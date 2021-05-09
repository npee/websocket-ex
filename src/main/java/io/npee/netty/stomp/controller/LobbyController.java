package io.npee.netty.stomp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.npee.netty.stomp.model.ChatMessage;
import io.npee.netty.stomp.model.ChatRoom;
import io.npee.netty.stomp.model.ChatUser;
import io.npee.netty.stomp.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LobbyController {

    private final ChatRoomRepository chatRoomRepository;

    @MessageMapping("/lobby.addUser")
    @SendTo("/topic/lobby")
    public ChatMessage addUserToLobby(@Payload ChatUser chatUser) {
        log.info("[JOIN] A User joined chat channel: [{}]", chatUser.getSender());
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setType(ChatMessage.MessageType.JOIN);
        chatMessage.setUserId(chatUser.getUserId());
        chatMessage.setSender(chatUser.getSender());
        chatMessage.setContent(chatUser.getSender() + " joined lobby!");
        return chatMessage;
    }

    @MessageMapping("/lobby.addRoom")
    @SendTo("/topic/lobby")
    public ChatRoom addRoom(@Payload ChatMessage chatMessage) throws JsonProcessingException {
        log.info("[CREATE] User [{}] created new room: [{}]", chatMessage.getSender(), chatMessage.getContent());
        // ChatRoom chatRoom = ChatRoom.create(chatMessage.getContent());
        ChatRoom chatRoom = chatRoomRepository.createChatRoom(chatMessage.getContent());
        // TODO: return room list
        return chatRoom;
    }

}
