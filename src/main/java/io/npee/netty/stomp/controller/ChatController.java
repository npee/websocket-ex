package io.npee.netty.stomp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.npee.netty.stomp.listener.RedisChatPublisher;
import io.npee.netty.stomp.listener.RedisChatSubscriber;
import io.npee.netty.stomp.model.ChatMessage;
import io.npee.netty.stomp.model.ChatUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final RedisMessageListenerContainer redisListener;
    private final RedisChatSubscriber redisChatSubscriber;
    private final RedisChatPublisher redisChatPublisher;

    @MessageMapping("/chat.sendMessage")
    // @SendTo("/topic/public")
    public void sendMessage(@Payload ChatMessage chatMessage) throws JsonProcessingException {
        log.info("payload(ChatMessage): {}", chatMessage);
        ChannelTopic topic = new ChannelTopic(chatMessage.getRoomId());
        redisChatPublisher.publish(topic, chatMessage);
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage) {
        ChatUser chatUser = new ChatUser();
        ChannelTopic topic = ChannelTopic.of(chatMessage.getRoomId());
        redisListener.addMessageListener(redisChatSubscriber, topic);
        chatUser.setSender(chatMessage.getSender());
        return chatMessage;
    }
}