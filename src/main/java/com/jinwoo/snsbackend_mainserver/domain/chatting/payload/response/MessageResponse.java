package com.jinwoo.snsbackend_mainserver.domain.chatting.payload.response;


import com.jinwoo.snsbackend_mainserver.domain.chatting.entity.Message;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.Notice;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    private String notice;

    private List<Message> messageList;

}
