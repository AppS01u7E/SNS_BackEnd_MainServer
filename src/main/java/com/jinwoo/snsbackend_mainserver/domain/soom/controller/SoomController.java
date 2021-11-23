package com.jinwoo.snsbackend_mainserver.domain.soom.controller;


import com.jinwoo.snsbackend_mainserver.domain.soom.dto.request.*;
import com.jinwoo.snsbackend_mainserver.domain.soom.dto.response.NoticeResponse;
import com.jinwoo.snsbackend_mainserver.domain.soom.dto.response.SoomInfoResponse;
import com.jinwoo.snsbackend_mainserver.domain.soom.dto.response.SoomShortResponse;
import com.jinwoo.snsbackend_mainserver.domain.soom.service.SoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/soom")
public class SoomController {
    private final SoomService soomService;


    @PostMapping("/teacher")
    public String teacherGeneSoom(TeacherGeneSoomRequest teacherGeneSoomRequest){
        return soomService.teacherGeneSoom(teacherGeneSoomRequest);
    }

    @PostMapping("/upgrade/{soomRoomId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void upgradeSoom(@PathVariable String soomRoomId){
        soomService.upgradeToClub(soomRoomId);
    }


    @PostMapping
    public String geneSoomRoom(@Valid@RequestBody GeneSoomRequest geneSoomRequest){
        return soomService.geneSoom(geneSoomRequest);
    }


    @PostMapping("/profile")
    public String postSoomProfile(@RequestParam String soomId, @Valid@ModelAttribute ProfilephotosRequest profilephotosRequest){
        return soomService.postSoomProfile(soomId, profilephotosRequest);
    }


    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSoomRoom(@RequestParam String soomId){
        soomService.deleteSoom(soomId);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void editSoomRoom(@RequestParam String soomId, @RequestBody TeacherGeneSoomRequest request){
        soomService.editSoom(soomId, request);
    }

    @GetMapping
    public SoomInfoResponse soomInfo(@RequestParam String soomId){
        return soomService.getSepSoomInfo(soomId);
    }




    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
    public void joinSoomRoom(@RequestBody JoinSoomRequest request){
        soomService.joinSoom(request);
    }


    @PostMapping("/code")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String checkSoomCode(@RequestParam String soomId){
        return soomService.checkSoomJoinCode(soomId);
    }


    @PostMapping("/notice")
    public ResponseEntity<?> postNotice(@Valid @RequestBody PostNoticeRequest request) {
        soomService.postNotice(request);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/notice/{soomId}/picture/{noticeId}")
    public ResponseEntity<?> uploadNoticeProfile(@Valid @PathVariable Long noticeId, @Valid @RequestParam String soomId, @Valid @ModelAttribute NoticeFileUploadRequest noticeFileUploadRequest){
        soomService.addFileOnNotice(noticeId, soomId, noticeFileUploadRequest);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{soomRoomId}/notice/{noticeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNotice(@PathVariable String soomRoomId, @PathVariable Long noticeId, @RequestBody String fileKey){
        soomService.deleteFile(noticeId, soomRoomId, fileKey);
    }


    @PatchMapping("/notice")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void editNotice(@RequestParam Long noticeId, @RequestBody PostNoticeRequest request){
        soomService.editNotice(noticeId, request);
    }

    @DeleteMapping("/notice")
    @ResponseStatus(HttpStatus.NO_CONTENT)
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
    @ResponseStatus(HttpStatus.CREATED)
    public void postComment(@RequestBody PostCommentRequest request){
        soomService.postComment(request);
    }


    @PatchMapping("/comment")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void editComment(@RequestBody EditCommentRequest request){
        soomService.editComment(request);
    }

    @DeleteMapping("/comment")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@RequestParam Long noticeId, @RequestParam Long commentId){
        soomService.deleteComment(noticeId, commentId);
    }


}
