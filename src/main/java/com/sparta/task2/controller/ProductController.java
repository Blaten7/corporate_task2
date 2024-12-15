package com.sparta.task2.controller;

import com.sparta.task2.dto.ProductRequestDto;
import com.sparta.task2.entity.Product;
import com.sparta.task2.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService pSer;

    // Constructor-based Dependency Injection
    public ProductController(ProductService productService) {
        this.pSer = productService;
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addProduct(@RequestBody Product product) {
        pSer.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
