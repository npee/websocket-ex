package io.npee.netty.stomp.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash("CHAT_USER")
public class ChatUser {
    @Id
    private String userId;
    private String name;
}
