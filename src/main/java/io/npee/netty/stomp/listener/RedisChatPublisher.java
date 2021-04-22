package io.npee.netty.stomp.listener;

import io.npee.netty.stomp.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisChatPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(ChannelTopic topic, ChatMessage message) {
        String topic1 = topic.getTopic();
        log.info("RedisChatPublisher - topic: {}", topic1);
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }

}
