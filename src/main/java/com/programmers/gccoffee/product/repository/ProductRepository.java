package com.programmers.gccoffee.product.repository;

import com.programmers.gccoffee.product.model.Product;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    Product insert(Product product);

    List<Product> findAll();

    Optional<Product> findById(UUID id);

    boolean deleteById(UUID id);

    Product update(Product product);

    void deleteAll();
}
