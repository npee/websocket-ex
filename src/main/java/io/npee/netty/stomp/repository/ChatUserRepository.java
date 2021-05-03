package io.npee.netty.stomp.repository;

import io.npee.netty.stomp.model.ChatUser;
import org.springframework.data.repository.CrudRepository;

public interface ChatUserRepository extends CrudRepository<ChatUser, String> {
}
