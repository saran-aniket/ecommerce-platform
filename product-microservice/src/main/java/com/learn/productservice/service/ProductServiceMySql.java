package com.learn.productservice.service;

import com.learn.productservice.dto.ProductRequestDto;
import com.learn.productservice.dto.ProductResponseDto;
import com.learn.productservice.exception.ProductNotFoundException;
import com.learn.productservice.mapper.ProductMapper;
import com.learn.productservice.model.Category;
import com.learn.productservice.model.Inventory;
import com.learn.productservice.model.Product;
import com.learn.productservice.model.Seller;
import com.learn.productservice.repository.InventoryRepository;
import com.learn.productservice.repository.SellerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.learn.productservice.repository.CategoryRepository;
import com.learn.productservice.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service(value = "productServiceMySql")
public class ProductServiceMySql{
    private static final Logger log = LoggerFactory.getLogger(ProductServiceMySql.class);
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SellerRepository sellerRepository;
    private final InventoryRepository inventoryRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public ProductServiceMySql(ProductRepository productRepository, CategoryRepository categoryRepository,
                               RedisTemplate<String, Object> redisTemplate, InventoryRepository inventoryRepository,
                               SellerRepository sellerRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.sellerRepository = sellerRepository;
        this.inventoryRepository = inventoryRepository;
        this.redisTemplate = redisTemplate;
    }

    public ProductResponseDto saveProduct(ProductRequestDto productRequestDto){
        Product product = ProductMapper.toProduct(productRequestDto);
        Optional<Category> getCategory = categoryRepository.findByNameIgnoreCase(productRequestDto.getCategory_name());
        Category category;
        if(getCategory.isEmpty()){
            category = new Category();
            category.setName(productRequestDto.getCategory_name());
            categoryRepository.save(category);
        }else{
            category = getCategory.get();
        }
        product.setCategory(category);
        Product createdProduct = productRepository.save(product);
        Inventory inventory = createInventoryWithRandomSeller(createdProduct);
        inventoryRepository.save(inventory);
        return ProductMapper.toProductResponseDto(createdProduct);
    }

    public List<ProductResponseDto> getAllProducts(int pageNumber, int pageSize){
        return productRepository.findAll().stream().map(ProductMapper::toProductResponseDto).toList();
    }

    public void saveAllProducts(List<ProductRequestDto> productRequestDtoList){
        try{
            List<Product> productList = productRequestDtoList.stream().map(ProductMapper::toProduct).toList();
            for(Product product : productList){
                Optional<Category> getCategory = categoryRepository.findByNameIgnoreCase(product.getCategory().getName());
                Category category;
                if(getCategory.isEmpty()){
                    category = new Category();
                    category.setName(product.getCategory().getName());
                    categoryRepository.save(category);
                }else{
                    category = getCategory.get();
                }
                product.setCategory(category);
            }
            productRepository.saveAll(productList);
            for(Product product : productList){
                Inventory inventory = createInventoryWithRandomSeller(product);
                inventoryRepository.save(inventory);
            }
            log.info("Inventory created for all products");
        } catch (Exception e) {
            log.error("Error while saving products {}", e.getMessage());
        }
    }

    public ProductResponseDto getProductById(UUID id){
        Product productFromCache = (Product) redisTemplate.opsForHash().get("PRODUCTS", "PRODUCT_"+id);
        if(productFromCache != null){
            log.info("Product found in cache");
            return ProductMapper.toProductResponseDto(productFromCache);
        }else{
            Optional<Product> optionalProduct = productRepository.getProductById(id);
            if(optionalProduct.isEmpty()){
                throw new ProductNotFoundException("Product with id " + id + " not found");
            }else{
                Product product = optionalProduct.get();
                redisTemplate.opsForHash().put("PRODUCTS", "PRODUCT_"+id, product);
                return ProductMapper.toProductResponseDto(product);
            }
        }
    }

    private Inventory createInventoryWithRandomSeller(Product product){
        List<Seller> sellerList = sellerRepository.findAll();
        Random random = new Random();
        Seller seller = sellerList.get(random.nextInt(sellerList.size()));
        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setSeller(seller);
        inventory.setQuantity(10);
        return inventory;
    }
}
