package io.npee.netty.stomp.config;

import io.npee.netty.stomp.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener implements ApplicationListener {

    private final SimpMessageSendingOperations messagingTemplate;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        log.info("application event fired [{}]", event.getClass());
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionAttributes().get("sessionId").toString();
        log.info("event -> [CONNECT] sessionId: {}", sessionId);
    }

    @EventListener
    public void handleWebSocketConnectedListener(SessionConnectedEvent event) {
        log.info("event -> [CONNECTED] Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String sessionId = headerAccessor.getSessionAttributes().get("sessionId").toString();
        log.info("event -> [DISCONNECTED] sessionId: {}", sessionId);

        if(username != null) {
            log.info("User Disconnected : " + username);

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(ChatMessage.MessageType.LEAVE);
            chatMessage.setSender(username);

            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}
