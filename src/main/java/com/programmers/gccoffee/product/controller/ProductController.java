package com.programmers.gccoffee.product.controller;

import com.programmers.gccoffee.product.service.ProductService;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class ProductController {

    private Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductService productService;

    @GetMapping("/product")
    public String newProduct() {
        return "product/new-product";
    }

    @PostMapping("/product")
    public String create(ProductDto productDto) {
        try {
            productService.create(
                productDto.getProductName(),
                productDto.getCategory(), productDto.getPrice(),
                productDto.getDescription());

            return "redirect:/";
        } catch (RuntimeException e) {
            return "product/error";
        }
    }

    @GetMapping("/products")
    public String list(Model model) {
        var products = productService.getProducts();
        model.addAttribute("products", products);

        return "product/list";
    }

    @GetMapping("/products/{id}")
    public String findById(@PathVariable UUID id, Model model) {
        var product = productService.findById(id).get();
        model.addAttribute("product", product);

        return "product/detail";
    }

    @DeleteMapping("/products/{productId}")
    public String deleteById(@PathVariable("productId") UUID productId) {
        productService.deleteById(productId);

        return "redirect:/products";
    }

    @PutMapping("/products/{id}")
    public String update(@PathVariable UUID id,
        @Validated ProductDto productDto) {

        if (productService.findById(id).isEmpty()) {
            return "product/error";
        }

        productService.update(id, productDto.getProductName(),
            productDto.getCategory(), productDto.getPrice(),
            productDto.getDescription());

        return "redirect:/products";
    }

    @GetMapping("/")
    public String productMainPage() {
        return "/product/main";
    }
}