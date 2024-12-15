package com.sparta.task2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestockNotificationRequest {
    private Long productId;
    private Long quantity;
}
