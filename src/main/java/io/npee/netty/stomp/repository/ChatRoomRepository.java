package io.npee.netty.stomp.repository;

import io.npee.netty.stomp.listener.RedisChatSubscriber;
import io.npee.netty.stomp.model.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ChatRoomRepository {

    private final RedisMessageListenerContainer listenerContainer;
    private final RedisChatSubscriber subscriber;

    private static final String CHAT_ROOMS = "CHAT_ROOMS";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, ChatRoom> opsHashChatRooms;
    private HashMap<String, ChannelTopic> channelTopics;

    @PostConstruct
    public void init() {
        this.opsHashChatRooms = redisTemplate.opsForHash();
        channelTopics = new HashMap<>();
    }

    public ChatRoom createChatRoom(String name) {
        ChatRoom room = ChatRoom.create(name);
        opsHashChatRooms.put(CHAT_ROOMS, room.getRoomId(), room);
        return room;
    }

    public void enterChatRoom(String roomId) {
        ChannelTopic topic = channelTopics.get(roomId);
        if (topic == null) {
            topic = new ChannelTopic(roomId);
            listenerContainer.addMessageListener(subscriber, topic);
            log.info("roomId - topic: {} - {}", roomId, topic);
            channelTopics.put(roomId, topic);
            for (String key : channelTopics.keySet()) {
                log.info("key: {}", key);
            }
        }
    }

}
