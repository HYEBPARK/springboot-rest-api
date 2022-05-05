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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {

    private Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductService productService;

    @GetMapping("/product/new-product")
    public String newProduct() {
        return "product/new-product";
    }

    @PostMapping("/")
    public String create(ProductDto productDto) {
        productService.create(
            productDto.getProductName(),
            productDto.getCategory(), productDto.getPrice(),
            productDto.getDescription());

        return "redirect:/";
    }

    @GetMapping("/product/list")
    public String list(Model model) {
        var products = productService.getProducts();
        model.addAttribute("products", products);

        return "product/list";
    }

    @GetMapping("/product/list/{id}")
    public String findById(@PathVariable UUID id, Model model) {
        var product = productService.findById(id).get();
        model.addAttribute("product", product);

        return "product/detail";
    }

    @DeleteMapping("/product/list")
    public String deleteById(@RequestParam UUID id) {
        productService.deleteById(id);

        return "redirect:/product/list";
    }

    @PostMapping("/product/list/{id}")
    public String update(@PathVariable UUID id,
        @Validated ProductDto productDto) {

        if (productService.findById(id).isEmpty()) {
            return "product/error";
        }

        productService.update(id, productDto.getProductName(),
            productDto.getCategory(), productDto.getPrice(),
            productDto.getDescription());

        return "redirect:/product/list";
    }

    @GetMapping("/")
    public String productMainPage() {
        return "/product/main";
    }
}