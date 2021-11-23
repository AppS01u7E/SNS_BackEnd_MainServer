package com.jinwoo.snsbackend_mainserver.domain.chatting.service;


import com.jinwoo.snsbackend_mainserver.domain.auth.dao.MemberRepository;
import com.jinwoo.snsbackend_mainserver.domain.auth.exception.MemberNotFoundException;
import com.jinwoo.snsbackend_mainserver.domain.chatting.dao.MessageRepository;
import com.jinwoo.snsbackend_mainserver.domain.chatting.entity.Message;
import com.jinwoo.snsbackend_mainserver.domain.chatting.exception.TooBigSizeException;
import com.jinwoo.snsbackend_mainserver.domain.chatting.payload.request.FileMessageRequest;
import com.jinwoo.snsbackend_mainserver.domain.chatting.payload.response.ChattingRoomListResponse;
import com.jinwoo.snsbackend_mainserver.domain.chatting.payload.response.MessageResponse;
import com.jinwoo.snsbackend_mainserver.domain.soom.dao.SoomRepository;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.SoomRoom;
import com.jinwoo.snsbackend_mainserver.domain.soom.exception.SoomNotFoundException;
import com.jinwoo.snsbackend_mainserver.global.exception.DataCannotBringException;
import com.jinwoo.snsbackend_mainserver.global.firebase.service.FcmService;
import com.jinwoo.snsbackend_mainserver.global.utils.CurrentMember;
import com.jinwoo.snsbackend_mainserver.global.utils.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChattingRoomServiceImpl implements ChattingRoomService{
    private final CurrentMember currentMember;
    private final MessageRepository messageRepository;
    private final SoomRepository soomRepository;
    private final S3Util s3Util;
    private final FcmService fcmService;
    private final MemberRepository memberRepository;



    @Override
    public List<ChattingRoomListResponse> getChattingRoomList(){
        return soomRepository.findAllByMemberIdsContains(currentMember.getMemberPk()).stream().map(
                chattingRoom -> chattingRoomToChattingRoomResponse(chattingRoom)
        ).collect(Collectors.toList());
    }

    @Override
    public MessageResponse getMessages(String chattingRoomId){
        return messageListToMessageResponse(messageRepository.findAllBySoomRoom(soomRepository.findById(chattingRoomId).orElseThrow(SoomNotFoundException::new)));
    }

    @Override
    public URL postImageMessage(FileMessageRequest request, String chattingRoomId){
        if (request.getFile().getSize() >= 2000000) throw new TooBigSizeException(request.getFile().getSize(), 50000000);

        soomRepository.findById(chattingRoomId).map(
                room -> {
                    try {
                        return new URL(s3Util.upload(request.getFile(), "CHAT/IMAGE", room.getId()));
                    } catch (IOException e) {
                        throw new DataCannotBringException();
                    }
                }
        );
        throw new SoomNotFoundException();
    }

    @Override
    public URL postFileMessage(FileMessageRequest request, String chattingRoomId){
        if (request.getFile().getSize() >= 50000000) throw new TooBigSizeException(request.getFile().getSize(), 50000000);

        soomRepository.findById(chattingRoomId).map(
                soomRoom -> {
                    try {
                        return new URL(s3Util.upload(request.getFile(), "CHAT/FILE", soomRoom.getId()));
                    } catch (IOException e){
                        throw new DataCannotBringException();
                    }
                }
        );
        throw new SoomNotFoundException();
    }


    private ChattingRoomListResponse chattingRoomToChattingRoomResponse(SoomRoom chattingRoom){
        return ChattingRoomListResponse.builder()
                .id(chattingRoom.getId())
                .info(chattingRoom.getInfo())
                .profiles(chattingRoom.getProfiles())
                .representativeId(chattingRoom.getRepresentativeId())
                .soomType(chattingRoom.getSoomType())
                .teacherId(chattingRoom.getTeacherId())
                .title(chattingRoom.getTitle())
                .memberIds(chattingRoom.getMemberIds())
                .build();
    }

    private MessageResponse messageListToMessageResponse(List<Message> messageList){
        return MessageResponse.builder()
                .notice(messageList.get(0).getSoomRoom().getLatestNotice().getTitle())
                .messageList(messageList)
                .build();
    }



    private void sendExceptChatIgnoreList(List<String> receivers, String title, String info) {
        try {
            log.info("sendExceptChatIgnoreList: SENDER: "+currentMember.getMemberPk()+", TITLE: "+title);
            fcmService.sendMessageRangeTo(receivers.stream().filter(
                    s -> !memberRepository.findById(s).map(
                            member -> member.getChatIgnoreList().contains(s)
                    ).orElseThrow(MemberNotFoundException::new)
            ).collect(Collectors.toList()), title, info);
        } catch (IOException e){
            throw new DataCannotBringException();
        }
    }



}
