package com.jinwoo.snsbackend_mainserver.domain.soom.service;

import com.jinwoo.snsbackend_mainserver.domain.soom.dto.request.*;
import com.jinwoo.snsbackend_mainserver.domain.soom.dto.response.NoticeResponse;
import com.jinwoo.snsbackend_mainserver.domain.soom.dto.response.SoomInfoResponse;
import com.jinwoo.snsbackend_mainserver.domain.soom.dto.response.SoomShortResponse;

import java.util.List;

public interface SoomService {

    public String teacherGeneSoom(TeacherGeneSoomRequest teacherGeneSoomRequest);
    public void upgradeToClub(String soomId);

    public String postSoomProfile(String soomId, ProfilephotosRequest request);

    public String geneSoom(GeneSoomRequest geneSoomRequest);

    public void joinSoom(JoinSoomRequest request);
    public void deleteSoom(String soomId);
    public void editSoom(String soomId, TeacherGeneSoomRequest teacherGeneSoomRequest);
    public SoomInfoResponse getSepSoomInfo(String soomId);

    public void postNotice(PostNoticeRequest request);

    public String checkSoomJoinCode(String soomId);
    public void editNotice(Long noticeId, PostNoticeRequest request);
    public void deleteNotice(Long noticeId, String soomId);

    public List<NoticeResponse> getSepSoomRoomNoticeList(String soomId, int page);
    public List<SoomShortResponse> includedSoom();
    public List<SoomShortResponse> searchTitleSoom(String search, int page);
    public List<SoomShortResponse> allSoomList(int page);

    public void postComment(PostCommentRequest request);
    public void editComment(EditCommentRequest request);
    public void deleteComment(Long noticeId, Long commentId);

    public void addFileOnNotice(Long noticeId, String soomRoomId, NoticeFileUploadRequest request);
    public void deleteFile(Long noticeId, String soomRoomId, String fileKey);

}
