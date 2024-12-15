package com.sparta.task2.component;

import com.sparta.task2.dto.NotificationRequestDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class RestockNotificationConsumer {

    private final RedisTemplate<String, NotificationRequestDto> redisTemplate;
    private final NotificationSender notificationSender;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public RestockNotificationConsumer(RedisTemplate<String, NotificationRequestDto> redisTemplate,
                                       NotificationSender notificationSender) {
        this.redisTemplate = redisTemplate;
        this.notificationSender = notificationSender;
        startConsuming();
    }

    // 소비자 시작
    private void startConsuming() {
        executorService.scheduleAtFixedRate(this::processNotifications, 0, 1, TimeUnit.SECONDS);
    }

    // Redis 큐에서 알림 요청 처리
    private void processNotifications() {
        for (int i = 0; i < 500; i++) { // 초당 최대 500개의 요청 처리
            NotificationRequestDto request = redisTemplate.opsForList().leftPop("restock-notifications");

            if (request == null) {
                break; // 큐가 비어 있으면 종료
            }

            notificationSender.sendNotification(request.getUserId(), request.getProductId());
        }
    }
}
