package io.npee.netty.stomp.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final ObjectMapper mapper;

    private final RedisMessageListenerContainer listenerContainer;
    private final RedisChatSubscriber subscriber;

    private static final String CHAT_ROOMS = "CHAT_ROOMS";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, String> opsHashChatRooms;
    private HashMap<String, ChannelTopic> channelTopics;

    @PostConstruct
    public void init() {
        this.opsHashChatRooms = redisTemplate.opsForHash();
        channelTopics = new HashMap<>();
    }

    public String createChatRoom(String name) throws JsonProcessingException {
        ChatRoom room = ChatRoom.create(name);
        String roomAsString = mapper.writeValueAsString(room);
        opsHashChatRooms.put(CHAT_ROOMS, room.getRoomId(), roomAsString);
        return roomAsString;
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
