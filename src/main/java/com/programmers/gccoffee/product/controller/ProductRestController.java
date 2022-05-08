package com.programmers.gccoffee.product.controller;

import com.programmers.gccoffee.product.model.Product;
import com.programmers.gccoffee.product.service.ProductService;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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

    private Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    ProductService productService;

    @GetMapping("/api/v1/products")
    public ResponseEntity<List<Product>> getProducts() {
        var products = productService.getProducts();

        return ResponseEntity.ok(products);
    }

    @GetMapping("/api/v1/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable UUID id) {
        var product = productService.findById(id);

        if (product.isEmpty()) {
            log.error("Product Controller getProductById -> {}", id);

            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(product.get());
    }

    @PostMapping("/api/v1/product")
    public ResponseEntity<ProductDto> postProduct(@RequestBody @Validated ProductDto productDto,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(
                (error) -> log.error("productDto value error -> {}", error.getDefaultMessage()));

            return ResponseEntity.badRequest().body(productDto);
        }

        productService.create(productDto.getProductName(),
            productDto.getCategory(), productDto.getPrice(),
            productDto.getDescription());

        return ResponseEntity.ok(productDto);
    }


    @PutMapping("/api/v1/products/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable UUID id,
        @Validated ProductDto productDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(
                (error) -> log.error("update productDto value error -> {}",
                    error.getDefaultMessage()));

            return ResponseEntity.badRequest().body(productDto);
        }

        if (productService.findById(id).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        productService.update(id, productDto.getProductName(),
            productDto.getCategory(), productDto.getPrice(),
            productDto.getDescription());

        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/api/v1/products/{id}")
    public ResponseEntity<Boolean> deleteProductById(@PathVariable UUID id) {
        var isDelete = productService.deleteById(id);

        return isDelete ? ResponseEntity.ok(true) : ResponseEntity.badRequest().build();
    }
}

