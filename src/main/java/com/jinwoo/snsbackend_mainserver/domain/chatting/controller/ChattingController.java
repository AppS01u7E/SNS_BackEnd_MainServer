package com.jinwoo.snsbackend_mainserver.domain.chatting.controller;


import com.jinwoo.snsbackend_mainserver.domain.chatting.payload.request.FileMessageRequest;
import com.jinwoo.snsbackend_mainserver.domain.chatting.payload.response.ChattingRoomListResponse;
import com.jinwoo.snsbackend_mainserver.domain.chatting.payload.response.MessageResponse;
import com.jinwoo.snsbackend_mainserver.domain.chatting.service.ChattingRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URL;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChattingController {
    private final ChattingRoomService chattingRoomService;

    @PostMapping("/file")
    public URL uploadFileMessage(@Valid FileMessageRequest request, @RequestParam String chattingRoomId){
        return chattingRoomService.postFileMessage(request, chattingRoomId);
    }


    @PostMapping("/image")
    public URL uploadImageMessage(@Valid FileMessageRequest request, @RequestParam String chattingRoomId){
        return chattingRoomService.postImageMessage(request, chattingRoomId);
    }


    @GetMapping("/list")
    public List<ChattingRoomListResponse> getChattingRoomList(){
        return chattingRoomService.getChattingRoomList();
    }


    @GetMapping
    public MessageResponse getMessages(@RequestParam String chattingRoomId){
        return chattingRoomService.getMessages(chattingRoomId);
    }


}
