package com.sparta.task2.controller;

import com.sparta.task2.dto.RestockNotificationRequest;
import com.sparta.task2.service.RestockNotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class RestockNotificationController {

    private final RestockNotificationService restockNotificationService;

    public RestockNotificationController(RestockNotificationService restockNotificationService) {
        this.restockNotificationService = restockNotificationService;
    }

    /**
     * 상품별 재입고 알람을 신청한 유저 등록.
     */
    @PostMapping("/{productId}/notification/registerUser")
    public ResponseEntity<String> saveUserNotificationRestock(@PathVariable Long productId) {
        restockNotificationService.saveUserNotification(productId);
        return ResponseEntity.ok("User notification registered successfully.");
    }

    /**
     * 일반 사용자용 재입고 알림 전송 API
     */
    @PostMapping("/{productId}/notifications/re-stock")
    public ResponseEntity<String> sendRestockNotifications(@RequestBody RestockNotificationRequest rnr) {
        restockNotificationService.processRestockNotifications(rnr);
        return ResponseEntity.ok("Normal User notification send successfully.");
    }

    /**
     * 관리자를 위한 수동 재입고 알림 전송 API
     */
    @PostMapping("/admin/{productId}/notifications/re-stock")
    public ResponseEntity<String> sendManualRestockNotifications(@RequestBody RestockNotificationRequest rnr) {
        restockNotificationService.processRestockNotificationsForAdmin(rnr);
        return ResponseEntity.ok("Admin notification send successfully.");
    }
}
