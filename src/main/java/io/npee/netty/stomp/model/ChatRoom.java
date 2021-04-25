package io.npee.netty.stomp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ChatRoom {
    private String roomId;
    private String roomName;

    public static ChatRoom create(String roomName) {
        ChatRoomBuilder chatRoomBuilder = ChatRoom.builder()
                .roomId(UUID.randomUUID().toString())
                .roomName(roomName);
        return chatRoomBuilder.build();
    }
}