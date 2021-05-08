package io.npee.netty.stomp.model;

import lombok.Data;

@Data
public class ChatMessage {
    private MessageType type;
    private String content;
    private String userId;
    private String sender;

    public enum MessageType {
        CHAT,
        CREATE,
        JOIN,
        ENTER,
        LEAVE
    }
}