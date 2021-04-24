package io.npee.netty.stomp.model;

import lombok.Data;

@Data
public class ChatRoom {
    private String roomId;
    private String roomName;
}