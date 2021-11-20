package com.jinwoo.snsbackend_mainserver.domain.soom.controller;


import com.jinwoo.snsbackend_mainserver.domain.soom.dto.request.*;
import com.jinwoo.snsbackend_mainserver.domain.soom.dto.response.NoticeResponse;
import com.jinwoo.snsbackend_mainserver.domain.soom.dto.response.SoomInfoResponse;
import com.jinwoo.snsbackend_mainserver.domain.soom.dto.response.SoomShortResponse;
import com.jinwoo.snsbackend_mainserver.domain.soom.service.SoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/soom")
public class SoomController {
    private final SoomService soomService;


    @PostMapping
    public String geneSoomRoom(@ModelAttribute ProfilephotosRequest profilephotosRequest, @RequestBody GeneSoomRequest geneSoomRequest){
        return soomService.geneSoom(geneSoomRequest, profilephotosRequest);
    }

    @DeleteMapping
    public void deleteSoomRoom(@RequestParam String soomId){
        soomService.deleteSoom(soomId);
    }

    @PatchMapping
    public void editSoomRoom(@RequestParam String soomId, @RequestBody GeneSoomRequest request){
        soomService.editSoom(soomId, request);
    }

    @GetMapping
    public SoomInfoResponse soomInfo(@RequestParam String soomId){
        return soomService.getSepSoomInfo(soomId);
    }




    @PostMapping("/join")
    public void joinSoomRoom(@RequestBody JoinSoomRequest request){
        soomService.joinSoom(request);
    }


    @PostMapping("/code")
    public String checkSoomCode(@RequestParam String soomId){
        return soomService.checkSoomJoinCode(soomId);
    }


    @PostMapping("/notice")
    public void postNotice(@Valid @RequestBody PostNoticeRequest request, @ModelAttribute NoticeFileUploadRequest noticeFileUploadRequest) {
        if (noticeFileUploadRequest.getFiles().isEmpty()) soomService.postNotice(request);
        else {
            soomService.postNotice(request, noticeFileUploadRequest);
        }
    }

    @PostMapping("/notice")
    public void postNotice(){



    }


    @PatchMapping("/notice")
    public void editNotice(@RequestParam Long noticeId, @RequestBody PostNoticeRequest request){
        soomService.editNotice(noticeId, request);
    }

    @DeleteMapping("/notice")
    public void deleteNotice(@RequestParam Long noticeId, @RequestParam String soomId){
        soomService.deleteNotice(noticeId, soomId);
    }

    @GetMapping("/notice")
    public List<NoticeResponse> getSepSoomRoomNoticeList(@RequestParam String soomId, @RequestParam int page){
        return soomService.getSepSoomRoomNoticeList(soomId, page);
    }

    @GetMapping("/my")
    public List<SoomShortResponse> includedSoom(){
        return soomService.includedSoom();
    }

    @GetMapping("/search")
    public List<SoomShortResponse> searchTitle(@RequestParam String search, @RequestParam int page){
        return soomService.searchTitleSoom(search, page);
    }

    @GetMapping("/all")
    public List<SoomShortResponse> allSoomList(@RequestParam int page){
        return soomService.allSoomList(page);
    }


    @PostMapping("/comment")
    public void postComment(@RequestBody PostCommentRequest request){
        soomService.postComment(request);
    }


    @PatchMapping("/comment")
    public void editComment(@RequestBody EditCommentRequest request){
        soomService.editComment(request);
    }

    @DeleteMapping("/comment")
    public void deleteComment(@RequestParam Long noticeId, @RequestParam Long commentId){
        soomService.deleteComment(noticeId, commentId);
    }




}
