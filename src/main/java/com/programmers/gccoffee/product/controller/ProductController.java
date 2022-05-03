package com.programmers.gccoffee.product.controller;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/product/new-product")
    public String newProduct() {
        return "product/new-product";
    }

    @PostMapping("/product")
    public String create(CreateProductRequest createProductRequest) {
        productService.create(
            createProductRequest.getProductName(),
            createProductRequest.getCategory(), createProductRequest.getPrice(),
            createProductRequest.getDescription());

        return "redirect:/product";
    }

    @GetMapping("/product/list")
    public String list(Model model) {
        model.addAttribute(productService.getProducts());

        return "product/list";
    }

    @GetMapping("/product/list/{id}")
    public String findById(@PathVariable UUID id, Model model) {
        var product = productService.findById(id);
        model.addAttribute(product);

        return "product/list/detail";
    }

    @DeleteMapping("/product/list")
    public String deleteById(@RequestParam UUID id) {
        productService.deleteById(id);

        return "redirect:/product/list";
    }

    @PostMapping("/product/list/{id}")
    public String update(@PathVariable UUID id, CreateProductRequest createProductRequest) {
        var maybeProduct = productService.findById(id);

        if (maybeProduct.isPresent()) {
            return "product/error";
        }

        var updateProduct = maybeProduct.get().updateProduct(createProductRequest.getProductName(),
            createProductRequest.getCategory(), createProductRequest.getPrice(),
            createProductRequest.getDescription());
        productService.update(updateProduct);
        return "redirect:/product/list";
    }

    @GetMapping("/product")
    public String productMainPage() {
        return "/product/main";
    }
}
