package io.npee.netty.stomp.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper mapper;

    public void publish(ChannelTopic topic, ChatMessage message) throws JsonProcessingException {
        String topic1 = topic.getTopic();
        log.info("RedisChatPublisher - topic: {}", topic1);
        redisTemplate.convertAndSend(topic.getTopic(), mapper.writeValueAsString(message));
    }

}
