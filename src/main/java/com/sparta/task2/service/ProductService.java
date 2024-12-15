package com.sparta.task2.service;

import com.sparta.task2.entity.Product;
import com.sparta.task2.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public void addProduct(Product product) {
        productRepository.save(product); // 데이터베이스에 저장
        long id = product.getProductId();
        productRepository.updateStockStatusById(id);
    }
}
