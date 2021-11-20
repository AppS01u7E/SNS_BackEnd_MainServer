package com.jinwoo.snsbackend_mainserver.domain.chatting.payload.response;


import com.jinwoo.snsbackend_mainserver.domain.soom.entity.Notice;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.SoomRoom;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.SoomType;
import lombok.*;

import java.net.URL;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChattingRoomListResponse {

    private String id;

    private String representativeId;

    private List<String> memberIds;

    private String title;

    private String info;

    private String teacherId;

    private List<URL> profiles;

    private SoomType soomType;


}
