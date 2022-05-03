package com.programmers.gccoffee.product.controller;

import com.programmers.gccoffee.product.model.Category;
import com.programmers.gccoffee.product.model.Product;
import com.programmers.gccoffee.product.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product create(String productName, Category category,
        long price, String description) {
        var product = new Product(UUID.randomUUID(), productName, category, price, description);

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

    public Product update(Product product) {
        return productRepository.update(product);
    }
}
