package io.npee.netty.stomp.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.npee.netty.stomp.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisChatSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("onMessage");
        String pubMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
        // String pubPattern = (String) redisTemplate.getStringSerializer().deserialize(pattern);
        try {
            ChatMessage chatMessage = objectMapper.readValue(pubMessage, ChatMessage.class);
            simpMessageSendingOperations.convertAndSend("/topic/public", chatMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
