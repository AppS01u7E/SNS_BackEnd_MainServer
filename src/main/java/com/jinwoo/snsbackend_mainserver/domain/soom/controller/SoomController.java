package com.jinwoo.snsbackend_mainserver.domain.soom.controller;


import com.jinwoo.snsbackend_mainserver.domain.soom.dto.request.*;
import com.jinwoo.snsbackend_mainserver.domain.soom.dto.response.DetailNoticeResponse;
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
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/soom")
public class SoomController {
    private final SoomService soomService;


    @PostMapping("/teacher")
    public SoomInfoResponse teacherGeneSoom(TeacherGeneSoomRequest teacherGeneSoomRequest){
        return soomService.teacherGeneSoom(teacherGeneSoomRequest);
    }

    @PostMapping("/upgrade/{soomRoomId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void upgradeSoom(@PathVariable String soomRoomId){
        soomService.upgradeToClub(soomRoomId);
    }

    @PostMapping
    public SoomInfoResponse geneSoomRoom(@Valid@RequestBody GeneSoomRequest geneSoomRequest){
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
    public void editSoomRoom(@RequestParam String soomId, @RequestBody EditSoomRequest request){
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

    @GetMapping("/out")
    public void getOutSoom(@RequestParam String soomId){
        soomService.getOutSoom(soomId);
    }

    @PostMapping("/code")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String checkSoomCode(@RequestParam String soomId){
        return soomService.checkSoomJoinCode(soomId);
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

    @PostMapping("/rep")
    public void chownRep(@RequestParam String soomId, String memberId){
        soomService.movePreviliege(soomId, memberId);
    }






    @GetMapping("/notice/list")
    public List<NoticeResponse> getSepSoomRoomNoticeList(@RequestParam String soomId, @RequestParam int page){
        return soomService.getSepSoomRoomNoticeList(soomId, page);
    }

    @GetMapping("/notice")
    public DetailNoticeResponse getOneNotice(@RequestParam Long noticeId){
        return soomService.getNotice(noticeId);
    }

    @PostMapping("/notice")
    @ResponseStatus(HttpStatus.CREATED)
    public void postNotice(@Valid @RequestBody PostNoticeRequest request) {
        soomService.postNotice(request);
    }

    @PostMapping("/notice/picture/{noticeId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadNoticeProfile(@Valid @PathVariable Long noticeId, @RequestParam String soomId, @Valid @ModelAttribute NoticeFileUploadRequest noticeFileUploadRequest){
        soomService.addFileOnNotice(noticeId, soomId, noticeFileUploadRequest);
        
    }

    @DeleteMapping("/{soomRoomId}/notice/{noticeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNoticeFile(@PathVariable String soomRoomId, @PathVariable Long noticeId, @RequestBody String fileUrl){
        soomService.deleteFile(noticeId, soomRoomId, fileUrl);
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




    @PostMapping("/notice/reply")
    @ResponseStatus(HttpStatus.CREATED)
    public void postReply(@RequestBody ReplyRequest request){
        soomService.postReply(request);
    }

    @PatchMapping("/notice/reply")
    @ResponseStatus(HttpStatus.CREATED)
    public void editReply(@RequestBody ReplyRequest request, @RequestParam Long id){
        soomService.editReply(request, id);
    }

    @DeleteMapping("/notice/reply")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReply(@RequestParam Long id){
        soomService.deleteReply(id);
    }


}
