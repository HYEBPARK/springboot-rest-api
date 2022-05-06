package com.programmers.gccoffee.product.controller;

import com.programmers.gccoffee.product.service.ProductService;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {

    private Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    ProductService productService;

    @GetMapping("/api/v1/products")
    public ResponseEntity getProducts() {
        var products = productService.getProducts();

        return ResponseEntity.ok(products);
    }

    @GetMapping("/api/v1/products/{id}")
    public ResponseEntity getProductById(@PathVariable UUID id) {
        var product = productService.findById(id);

        if (product.isEmpty()) {
            logger.error("Product Controller getProductById -> {}", id);

            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(product.get());
    }

    @PostMapping("/api/v1/product")
    public ResponseEntity postProduct(@RequestBody ProductDto productDto) {
        try {
            var product = productService.create(productDto.getProductName(),
                productDto.getCategory(), productDto.getPrice(),
                productDto.getDescription());

            return ResponseEntity.ok(product);
        } catch (IncorrectResultSizeDataAccessException e) {
            logger.error("Product Controller postProduct -> {}", productDto.toString());

            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/api/v1/products/{id}")
    public ResponseEntity deleteProductById(@PathVariable UUID id) {
        var isDelete = productService.deleteById(id);

        return isDelete ? ResponseEntity.ok(true) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/api/v1/products/{id}")
    public ResponseEntity updateProduct(@PathVariable UUID id,
        @Validated ProductDto productDto) {

        if (productService.findById(id).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var updateProduct = productService.update(id, productDto.getProductName(),
            productDto.getCategory(), productDto.getPrice(),
            productDto.getDescription());

        return ResponseEntity.ok(updateProduct);
    }
}