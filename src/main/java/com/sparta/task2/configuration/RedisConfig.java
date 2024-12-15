package com.sparta.task2.configuration;

import com.sparta.task2.dto.NotificationRequestDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, NotificationRequestDto> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, NotificationRequestDto> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // 직렬화
        template.setKeySerializer(new StringRedisSerializer());
        return template;
    }
}
