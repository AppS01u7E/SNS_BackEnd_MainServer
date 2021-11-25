package com.jinwoo.snsbackend_mainserver.domain.chatting.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.SoomRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JsonBackReference
    private SoomRoom soomRoom;



}
