package com.jinwoo.snsbackend_mainserver.domain.chatting.entity;


import com.jinwoo.snsbackend_mainserver.domain.soom.entity.SoomRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    private String id;

    private String message;

    private ChatType chatType;

    private String senderId;

    @ManyToOne
    private SoomRoom soomRoom;



}
