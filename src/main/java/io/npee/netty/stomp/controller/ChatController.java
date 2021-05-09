package io.npee.netty.stomp.controller;

import io.npee.netty.stomp.model.ChatMessage;
import io.npee.netty.stomp.model.ChatRoom;
import io.npee.netty.stomp.model.ChatUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.UUID;


@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        log.info("payload(ChatMessage): {}", chatMessage);
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage) {
        ChatUser chatUser = new ChatUser();
        // chatUser.setUserId(UUID.randomUUID().toString());
        chatUser.setSender(chatMessage.getSender());
        return chatMessage;
    }
}