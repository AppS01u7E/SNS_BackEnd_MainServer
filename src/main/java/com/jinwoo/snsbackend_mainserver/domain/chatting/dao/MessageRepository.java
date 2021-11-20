package com.jinwoo.snsbackend_mainserver.domain.chatting.dao;

import com.jinwoo.snsbackend_mainserver.domain.chatting.entity.Message;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.SoomRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String> {
    List<Message> findAllBySoomRoom(SoomRoom soomRoom);

}
