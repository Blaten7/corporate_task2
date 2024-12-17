//package com.sparta.task2.controller;
//
//import com.sparta.task2.dto.NotificationRequestDto;
//import com.sparta.task2.dto.RestockNotificationRequest;
//import com.sparta.task2.entity.Product;
//import com.sparta.task2.entity.ProductNotificationHistory;
//import com.sparta.task2.entity.ProductUserNotification;
//import com.sparta.task2.repository.ProductNotificationHistoryRepository;
//import com.sparta.task2.repository.ProductRepository;
//import com.sparta.task2.repository.ProductUserNotificationRepository;
//import com.sparta.task2.service.RestockNotificationService;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//@ActiveProfiles("test")  // 'test' 프로파일을 활성화
//public class NotificationServiceTest {
//
//    @Autowired
//    private RestockNotificationService restockNotificationService;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private ProductUserNotificationRepository productUserNotificationRepository;
//
//    @Autowired
//    private ProductNotificationHistoryRepository productNotificationHistoryRepository;
//
//    private Long productId;
//
//    @BeforeEach
//    public void setup() {
//        // 테스트용 데이터 준비
//        productId = prepareTestData();
//    }
//    @AfterEach
//    public void cleanup() {
//        productUserNotificationRepository.deleteAll();
//        productNotificationHistoryRepository.deleteAll();
//        productRepository.deleteAll();
//    }
//
//    private Long prepareTestData() {
//        // 1. 상품 생성
//        Product product = new Product();
//        product.setStockStatus(500);  // 충분한 재고 설정
//        product.setRestockRound(0);
//        productRepository.save(product);
//
//        // 2. 500명의 유저 생성 (중복 알림 체크)
//        List<ProductUserNotification> notifications = new ArrayList<>();
//        for (int i = 1; i <= 500; i++) {
//            // 유저의 알림이 이미 존재하는지 확인하여 중복 방지
//            if (!productUserNotificationRepository.existsByProductAndUserId(product, (long) i)) {
//                ProductUserNotification notification = new ProductUserNotification();
//                notification.setProduct(product);
//                notification.setUserId((long) i);
//                notification.setActive(true);  // 알림 활성화
//                notifications.add(notification);
//            }
//        }
//        // 중복을 제거한 유저 알림만 삽입
//        productUserNotificationRepository.saveAll(notifications);
//
//        return product.getProductId();
//    }
//
//
//    @Test
//    @Transactional
//    public void testSendRestockNotification() {
//        RestockNotificationRequest rnr = new RestockNotificationRequest();
//        rnr.setProductId(1L);
//        // 재입고 알림 전송
//        NotificationRequestDto response = restockNotificationService.processRestockNotifications(rnr.getProductId());
//
//        // 결과 확인 (500명 알림 전송)
//        List<ProductNotificationHistory> histories = productNotificationHistoryRepository.findAll();
//        assertEquals(500, histories.size(), "500명이 알림을 받아야 합니다.");
//
//        // 알림 상태 확인
//        for (ProductNotificationHistory history : histories) {
//            // enum의 name() 메서드를 사용하여 알림 상태 비교
//            assertEquals("COMPLETED", history.getNotificationStatus(), "알림 상태는 COMPLETED여야 합니다.");
//        }
//    }
//
//
//    @Test
//    public void testCodePerformance() {
//        // 코드 실행 시간 측정
//        long startTime = System.nanoTime();
//        restockNotificationService.sendRestockNotification(productId);
//        long endTime = System.nanoTime();
//
//        System.out.println("Code Execution Time: " + (endTime - startTime) / 1_000_000 + " ms");
//    }
//}