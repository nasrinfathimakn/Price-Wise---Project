package com.pricewise;

import com.pricewise.entity.Product;
import com.pricewise.repository.ProductRepository;
import com.pricewise.repository.RetailerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
class PriceWiseApplicationTests {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RetailerRepository retailerRepository;

    @Test
    @DisplayName("Application context loads and Flyway initializes demo seed data")
    void contextLoadsAndDataInitialized() {
        assertNotNull(productRepository);
        assertNotNull(retailerRepository);

        List<Product> products = productRepository.findAll();
        assertFalse(products.isEmpty(), "Products should be populated from Flyway seed migration");
        assertTrue(products.size() >= 5, "At least 5 sample products should exist");

        assertEquals(4, retailerRepository.count(), "4 retailers (Amazon, Flipkart, Croma, Reliance) should exist");
    }
}

