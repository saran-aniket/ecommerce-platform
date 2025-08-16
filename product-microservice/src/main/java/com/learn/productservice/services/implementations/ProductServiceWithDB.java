package com.learn.productservice.services.implementations;

import com.learn.productservice.dtos.ProductRequestDto;
import com.learn.productservice.exceptions.DuplicateProductFoundException;
import com.learn.productservice.exceptions.ProductNotFoundException;
import com.learn.productservice.mapper.ProductMapper;
import com.learn.productservice.model.Category;
import com.learn.productservice.model.Product;
import com.learn.productservice.repositories.CategoryRepository;
import com.learn.productservice.repositories.ProductRepository;
import com.learn.productservice.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service(value = "productServiceWithDb")
public class ProductServiceWithDB implements ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductServiceWithDB.class);
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public ProductServiceWithDB(ProductRepository productRepository, CategoryRepository categoryRepository,
                                RedisTemplate<String, Object> redisTemplate) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.redisTemplate = redisTemplate;
    }

    public Product saveProduct(ProductRequestDto productRequestDto) {
        log.info("Creating product");
        Product product = ProductMapper.toProduct(productRequestDto);
        Optional<Category> getCategory = categoryRepository.findByNameIgnoreCase(productRequestDto.getCategory_name());
        Category category;
        if (getCategory.isEmpty()) {
            category = new Category();
            category.setName(productRequestDto.getCategory_name());
            categoryRepository.save(category);
        } else {
            category = getCategory.get();
            Optional<Product> optionalProduct = productRepository.getProductByNameAndCategory_Name(product.getName(), category.getName());
            if (optionalProduct.isPresent()) {
                throw new DuplicateProductFoundException("Product already exists in this category");
            }
        }
        product.setCategory(category);
        Product createdProduct = productRepository.save(product);
        log.info("Inventory created for product {}", createdProduct.getName());
        return createdProduct;
    }

    public List<Product> getAllProducts(int pageNumber, int pageSize) {
        return productRepository.findAll().stream().toList();
    }

//    public void saveAllProducts(List<ProductRequestDto> productRequestDtoList) {
//        try {
//            List<Product> productList = productRequestDtoList.stream().map(ProductMapper::toProduct).toList();
//            for (Product product : productList) {
//                Optional<Category> getCategory = categoryRepository.findByNameIgnoreCase(product.getCategory().getName());
//                Category category;
//                if (getCategory.isEmpty()) {
//                    category = new Category();
//                    category.setName(product.getCategory().getName());
//                    categoryRepository.save(category);
//                } else {
//                    category = getCategory.get();
//                }
//                product.setCategory(category);
//            }
//            productRepository.saveAll(productList);
//        } catch (Exception e) {
//            log.error("Error while saving products {}", e.getMessage());
//        }
//    }

    public Product getProductById(UUID id) {
        Product productFromCache = (Product) redisTemplate.opsForHash().get("PRODUCTS", "PRODUCT_" + id);
        if (productFromCache != null) {
            log.info("Product found in cache");
            return productFromCache;
        } else {
            Optional<Product> optionalProduct = productRepository.getProductById(id);
            if (optionalProduct.isEmpty()) {
                throw new ProductNotFoundException("Product with id " + id + " not found");
            } else {
                Product product = optionalProduct.get();
                redisTemplate.opsForHash().put("PRODUCTS", "PRODUCT_" + id, product);
                return product;
            }
        }
    }
}
