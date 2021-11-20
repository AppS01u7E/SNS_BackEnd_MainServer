package com.jinwoo.snsbackend_mainserver.domain.chatting.service;

import com.jinwoo.snsbackend_mainserver.domain.chatting.payload.request.FileMessageRequest;
import com.jinwoo.snsbackend_mainserver.domain.chatting.payload.response.ChattingRoomListResponse;
import com.jinwoo.snsbackend_mainserver.domain.chatting.payload.response.MessageResponse;

import java.net.URL;
import java.util.List;

public interface ChattingRoomService {
    public URL postFileMessage(FileMessageRequest request, String chattingRoomId);
    public URL postImageMessage(FileMessageRequest request, String chattingRoomId);
    public List<ChattingRoomListResponse> getChattingRoomList();
    public MessageResponse getMessages(String chattingRoomId);

}
