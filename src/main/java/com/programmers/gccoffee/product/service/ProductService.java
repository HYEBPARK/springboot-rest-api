package com.programmers.gccoffee.product.service;

import com.programmers.gccoffee.product.model.Category;
import com.programmers.gccoffee.product.model.Product;
import com.programmers.gccoffee.product.repository.ProductRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product create(String productName, Category category,
        long price, String description) {
        var product = new Product(UUID.randomUUID(), productName, category, price, description);

        return productRepository.insert(product);
    }

    public Product create(UUID productId, String productName, Category category,
        long price, String description, LocalDateTime createdAt, LocalDateTime updatedAt) {
        var product = new Product(productId, productName, category, price, description,createdAt, updatedAt);

        return productRepository.insert(product);
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(UUID id) {
        return productRepository.findById(id);
    }

    public boolean deleteById(UUID id) {
        return productRepository.deleteById(id);
    }

    public Product update(UUID productId, String productName, Category category,
        long price, String description) {
        var product = productRepository.findById(productId).get();
        product.updateProduct(productName,category,price,description);

        return productRepository.update(product);
    }
}
