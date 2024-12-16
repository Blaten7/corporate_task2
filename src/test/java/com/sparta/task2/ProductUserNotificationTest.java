package com.sparta.task2;

import com.sparta.task2.entity.Product;
import com.sparta.task2.entity.ProductUserNotification;
import com.sparta.task2.repository.ProductRepository;
import com.sparta.task2.repository.ProductUserNotificationRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.Random;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class ProductUserNotificationTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductUserNotificationRepository productUserNotificationRepository;

    @BeforeEach
    public void resetDatabase() {
        try {
            // 데이터베이스 삭제
            jdbcTemplate.execute("DROP DATABASE IF EXISTS corpTask2");
            // 데이터베이스 다시 생성
            jdbcTemplate.execute("CREATE DATABASE corpTask2");
            System.out.println("Database corpTask2 reset completed.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to reset database corpTask2", e);
        }
    }

    @Test
    public void testBulkProductAndNotification() {
        Random random = new Random();

        // 1. 상품 10개 등록
        for (int i = 1; i <= 10; i++) {
            // 상품 생성
            Product product = new Product();
            product.setRestockRound(0); // 초기값 설정
            product.setStockStatus(random.nextInt(10000)); // 0 ~ 9999 사이 랜덤 재고 설정
            productRepository.save(product);

            // 2. 각 상품에 대해 랜덤 유저 알림 등록 (0~5000명)
            int userCount = random.nextInt(5001); // 0 ~ 5000
            for (int j = 1; j <= userCount; j++) {
                ProductUserNotification notification = new ProductUserNotification();
                notification.setProduct(product);
                notification.setUserId((long) j); // 유저 ID
                notification.setActive(true); // 알림 활성화
                notification.setCreatedAt(LocalDateTime.now());
                notification.setUpdatedAt(LocalDateTime.now());
                productUserNotificationRepository.save(notification);
            }
            // 로그 출력
            System.out.printf("Product %d created with %d user notifications%n", i, userCount);
        }
    }
}



