package com.learn.productservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {"jwt.secret=secret", "jwt.expiration=3600"})
class ProductServiceApplicationTest {

    @Test
    void contextLoads() {

    }
}
