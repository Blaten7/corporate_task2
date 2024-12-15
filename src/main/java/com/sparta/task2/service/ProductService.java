package com.sparta.task2.service;

import com.sparta.task2.entity.Product;
import com.sparta.task2.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.Lint;
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

    @Transactional
    public void updateProductCountStatus() {
        // 상품 테이블의 총 개수 조회
        long totalProducts = productRepository.count();

        Product product = new Product();
        product.setStockStatus((int) totalProducts);
        productRepository.save(product);
    }
}
