package io.npee.netty.stomp.controller;

import io.npee.netty.stomp.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.Set;

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
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        log.info("ChatMessage.getSender(): {}", chatMessage.getSender());
        log.info("SimpleMessageHeaderAccessor.getDestination: {}", headerAccessor.getDestination());
        // log.info("SimpleMessageHeaderAccessor.getDetailedLogMessage: {}", headerAccessor.getDetailedLogMessage(chatMessage));
        log.info("SimpleMessageHeaderAccessor.getMessageType: {}", headerAccessor.getMessageType());

        // log.info("SimpleMessageHeaderAccessor: {}", headerAccessor.getSessionAttributes());
        Set<Map.Entry<String, Object>> entries = headerAccessor.getSessionAttributes().entrySet();
        for (Map.Entry<String, Object> entry: entries) {
            log.info("\tsessionAttributes: {}", entry);
        }
        log.info("SimpleMessageHeaderAccessor.sessionId: {}", headerAccessor.getSessionId());
        log.info("SimpleMessageHeaderAccessor.getSubscriptionId: {}", headerAccessor.getSubscriptionId());
        log.info("SimpleMessageHeaderAccessor.getUser: {}", headerAccessor.getUser());
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}