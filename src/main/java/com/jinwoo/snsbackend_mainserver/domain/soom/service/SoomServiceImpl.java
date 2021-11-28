package com.jinwoo.snsbackend_mainserver.domain.soom.service;

import com.jinwoo.snsbackend_mainserver.domain.auth.dao.MemberRepository;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Role;
import com.jinwoo.snsbackend_mainserver.domain.auth.exception.InvalidCodeException;
import com.jinwoo.snsbackend_mainserver.domain.auth.exception.MemberNotFoundException;
import com.jinwoo.snsbackend_mainserver.domain.soom.dao.CommentRepository;
import com.jinwoo.snsbackend_mainserver.domain.soom.dao.NoticeRepository;
import com.jinwoo.snsbackend_mainserver.domain.soom.dao.ReplyMentRepository;
import com.jinwoo.snsbackend_mainserver.domain.soom.dao.SoomRepository;
import com.jinwoo.snsbackend_mainserver.domain.soom.dto.request.*;
import com.jinwoo.snsbackend_mainserver.domain.soom.dto.response.DetailNoticeResponse;
import com.jinwoo.snsbackend_mainserver.domain.soom.dto.response.NoticeResponse;
import com.jinwoo.snsbackend_mainserver.domain.soom.dto.response.SoomInfoResponse;
import com.jinwoo.snsbackend_mainserver.domain.soom.dto.response.SoomShortResponse;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.*;
import com.jinwoo.snsbackend_mainserver.domain.soom.exception.*;
import com.jinwoo.snsbackend_mainserver.global.exception.DataCannotBringException;
import com.jinwoo.snsbackend_mainserver.global.exception.LowAuthenticationException;
import com.jinwoo.snsbackend_mainserver.global.exception.UploadException;
import com.jinwoo.snsbackend_mainserver.global.firebase.service.FcmServiceImpl;
import com.jinwoo.snsbackend_mainserver.global.utils.CurrentMember;
import com.jinwoo.snsbackend_mainserver.global.utils.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SoomServiceImpl implements SoomService {

    private final ReplyMentRepository replyMentRepository;
    private final CurrentMember currentMember;
    private final S3Util s3Util;
    private final SoomRepository soomRepository;
    private final NoticeRepository noticeRepository;
    private final CommentRepository commentRepository;
    private final FcmServiceImpl fcmService;
    private final MemberRepository memberRepository;


    @Override
    public SoomInfoResponse teacherGeneSoom(TeacherGeneSoomRequest request) {
        try {
            return geneSoomRoomMain(request.getTitle(), request.getInfo(), request.getRepresentitiveId(), currentMember.getMemberPk(), request.getSoomType());
        } catch (Exception e) {
            throw new SoomException(e.getMessage());
        }
    }

    @Override
    public SoomInfoResponse geneSoom(GeneSoomRequest request) {
        return geneSoomRoomMain(request.getTitle(), request.getInfo(), currentMember.getMemberPk(), null, SoomType.ELSE);
    }

    @Override
    public void upgradeToClub(String soomId) {
        if (currentMember.getMember().getRole().equals(Role.ROLE_TEACHER)) {
            soomRepository.findById(soomId).map(
                    soomRoom -> soomRepository.save(
                            soomRoom.setRoomType(SoomType.CLUB).setTeacher(currentMember.getMemberPk())
                    )
            ).orElseThrow(SoomNotFoundException::new);
        }
        else throw new LowAuthenticationException();
    }

    @Override
    public String postSoomProfile(String soomId, ProfilephotosRequest request) {
        log.info(soomId);
        return soomRepository.findByIdAndRepresentativeId(soomId, currentMember.getMemberPk()).map(
                soomRoom -> {
                    try {
                        URL url = s3Util.getURL(s3Util.upload(request.getProfile(), "SOOM", soomRoom.getId())).getImageUrl();
                        soomRoom.addProfile(url);
                        soomRepository.save(soomRoom);
                        return url.toString();
                    } catch (IOException e) {
                        throw new  DataCannotBringException();
                    }
                }
            ).orElseThrow(SoomNotFoundException::new);
    }

    @Override
    public String checkSoomJoinCode(String soomId) {
        return (currentMember.getMember().getRole().equals(Role.ROLE_STUDENT) ?
                soomRepository.findByIdAndRepresentativeId(soomId, currentMember.getMemberPk()) : soomRepository.findById(soomId))
                .map(
                        SoomRoom::getJoinCode
                ).orElseThrow(SoomNotFoundException::new);
    }

    @Override
    public void joinSoom(JoinSoomRequest request) {
        String data = soomRepository.findById(request.getSoomId()).orElseThrow(SoomNotFoundException::new).getJoinCode();
        if (currentMember.getMember().getRole().equals(Role.ROLE_TEACHER)) {
            soomRepository.findById(request.getSoomId()).orElseThrow(SoomNotFoundException::new).setTeacher(currentMember.getMemberPk());
        }
        else if (data.equals(request.getJoinCode())) {
            soomRepository.findById(request.getSoomId()).ifPresentOrElse(
                    room -> {
                        room.addMember(currentMember.getMemberPk());
                        if (room.getMemberIds().size() >= 8) room.setRoomType(SoomType.TEAM);
                        soomRepository.save(room);
                        memberRepository.save(currentMember.getMember().joinSoom(room));
                    },
                    SoomNotFoundException::new
            );
        }
        else throw new InvalidCodeException();
    }
    //teacherAuth
    @Override
    public void deleteSoom(String soomId) {
        SoomRoom soomRoom = soomRepository.findById(soomId).orElseThrow(SoomNotFoundException::new);
        if (soomRoom.getSoomType().equals(SoomType.ELSE)&&soomRoom.getRepresentativeId().equals(currentMember.getMemberPk())){
            soomRepository.delete(soomRoom);
        }
        else if (soomRoom.getTeacherId().equals(currentMember.getMemberPk())){
            soomRepository.delete(soomRoom);
        }
        else throw new LowAuthenticationException();

    }
    //RepStu||teacherAuth
    @Override
    public void editSoom(String soomId, EditSoomRequest request) {
        noticeAuthenticationCheck(soomId);
        soomRepository.save(
                soomRepository.findById(soomId).map(
                        room -> room.editTitleAndInfo(request.getTitle(), request.getInfo())
                ).orElseThrow(SoomNotFoundException::new));
    }

    @Override
    public void getOutSoom(String soomId){
        SoomRoom soomRoom = soomRepository.findById(soomId).orElseThrow(SoomNotFoundException::new);
        if (soomRoom.getRepresentativeId().equals(currentMember.getMemberPk())) throw new CannotKillSoomHeaderException();
        else {
            soomRoom.getOutSoom(currentMember.getMemberPk());

            soomRepository.save(soomRoom);
        }
    }

    @Override
    public void movePreviliege(String soomId, String memberId){
        soomRepository.findById(soomId).map(
                soomRoom -> {
                    if (soomRoom.getRepresentativeId().equals(currentMember.getMemberPk())) throw new IsNotRepException();
                    return soomRepository.save(
                            soomRoom.movePreviliege(memberRepository.findById(
                                    memberId
                            ).map(
                                    member -> {
                                        if (!member.getSoomRooms().contains(soomRoom)) throw new IsNotSoomMemberException();
                                        return member;
                                    }
                                    ).orElseThrow(MemberNotFoundException::new).getId()
                            )
                    );
                }
        ).orElseThrow(SoomNotFoundException::new);

    }

    @Override
    public void changeTeacher(String soomId, String teacherId){
        soomRepository.save(
            soomRepository.findById(soomId).map(
                    soomRoom -> {
                        if (!soomRoom.getTeacherId().equals(currentMember.getMemberPk())) throw new LowAuthenticationException();
                        soomRoom.setTeacher(memberRepository.findById(teacherId).orElseThrow(MemberNotFoundException::new).getId());
                        log.info(soomRoom.getTitle()+"의 담당 선생님 변경 ->" + teacherId);
                        return soomRoom;
                    }
            ).orElseThrow(SoomNotFoundException::new)
        );
    }

    @Override
    public void kickOutMember(String soomId, String memberId){
        soomRepository.save(
            soomRepository.findById(soomId).map(
                    soomRoom ->{
                        if (!soomRoom.getRepresentativeId().equals(currentMember.getMemberPk())&&!soomRoom.getTeacherId().equals(currentMember.getMemberPk())) throw new LowAuthenticationException();
                        String kickId = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new).getId();
                        if (soomRoom.getRepresentativeId().equals(kickId)) throw new CannotKillSoomHeaderException();
                        soomRoom.getOutSoom(kickId);
                        return soomRoom;
                    }
            ).orElseThrow(MemberNotFoundException::new)
        );
    }



    @Override
    public void postNotice(PostNoticeRequest request) {
        SoomRoom soomRoom = noticeAuthenticationCheck(request.getSoomId());
        Notice notice = Notice.builder()
                        .title(request.getTitle())
                        .info(request.getInfo())
                        .fileKeys(new ArrayList<>())
                        .room(soomRoom)
                        .build();
        noticeRepository.save(notice);

        sendExceptNoticeIgnoreList(soomRoom.getMemberIds(), soomRoom.getTitle() + "에 공지가 등록되었습니다.", request.getTitle());
    }

    @Override
    public void addFileOnNotice(Long noticeId, String soomRoomId, NoticeFileUploadRequest request) {
        if (request.getFiles().size() + noticeRepository.findById(noticeId).orElseThrow(
                NoticeNotFoundException::new
        ).getFileKeys().size() >= 5) throw new TooManyFilesException();
        sendExceptNoticeIgnoreList(
                noticeRepository.save(noticeRepository.findByIdAndRoom(noticeId, noticeAuthenticationCheck(soomRoomId)).orElseThrow(NoticeNotFoundException::new)
                        .addFiles(noticeFileUpload(request, soomRoomId))).getRoom().getMemberIds(), "새로운 파일이 등록되었습니다", request.getFiles().get(0)
                        .getOriginalFilename() + " 외 " + request.getFiles().size() + "개의 파일"
        );
    }

    @Override
    public void deleteFile(Long noticeId, String soomId, String fileUrl) {
        noticeRepository.save(noticeRepository.findByIdAndRoom(noticeId, noticeAuthenticationCheck(soomId)).orElseThrow(NoticeNotFoundException::new)
                .deleteFile(fileUrl));
    }

    @Override
    @Transactional
    public void editNotice(Long noticeId, PostNoticeRequest request) {
        SoomRoom room = noticeAuthenticationCheck(request.getSoomId());
        Notice notice = noticeRepository.findByIdAndRoom(noticeId, room).orElseThrow(NoticeNotFoundException::new);
        noticeRepository.save(new Notice(notice.getId(), request.getTitle(), request.getInfo(), notice.getFileKeys()));
        sendExceptNoticeIgnoreList(notice.getRoom().getMemberIds(), "공지가 수정되었습니다.", "수정된 공지: " + notice.getTitle());
    }

    @Override
    @Transactional
    public void deleteNotice(Long noticeId, String soomId) {
        SoomRoom soomRoom = noticeAuthenticationCheck(soomId);
        Notice notice = noticeRepository.findByIdAndRoom(noticeId, soomRoom).orElseThrow(NoticeNotFoundException::new);
        noticeRepository.delete(notice);
        sendExceptNoticeIgnoreList(notice.getRoom().getMemberIds(), "공지가 삭제되었습니다.", "삭제된 공지: " + notice.getTitle());

    }

    @Override
    @Transactional
    public List<NoticeResponse> getSepSoomRoomNoticeList(String soomId, int page) {
        log.info(soomId +"   "+ currentMember.getMemberPk());
        SoomRoom room = soomRepository.findByIdAndRepresentativeId(soomId, currentMember.getMemberPk()).orElseThrow(SoomNotFoundException::new);
        return noticeToNoticeResponse(noticeRepository.findAllByRoomOrderByCreatedAtDesc(room, PageRequest.of(page, 20)).toList());
    }

    @Override
    public DetailNoticeResponse getNotice(Long noticeId){
        return noticeToDetailNoticeResponse(
                noticeRepository.findById(noticeId).orElseThrow(NoticeNotFoundException::new)
        );
    }




    @Override
    public List<SoomShortResponse> includedSoom() {
        log.info(currentMember.getMemberPk()+"- 소유 SOOM Lists");

        return soomRoomToSoomListResopnse(currentMember.getMember().getSoomRooms());
    }

    @Override
    public List<SoomShortResponse> searchTitleSoom(String search, int page) {
        return soomRoomToSoomListResopnse(soomRepository.findAllByTitleContains(search, PageRequest.of(page, 20)).toList());
    }

    @Override
    public List<SoomShortResponse> allSoomList(int page) {
        return soomRoomToSoomListResopnse(soomRepository.findAll(PageRequest.of(page, 20)).toList());
    }

    @Override
    public SoomInfoResponse getSepSoomInfo(String soomId) {
        return soomToSoomInfoResponse(soomRepository.findById(soomId).orElseThrow(SoomNotFoundException::new));
    }




    @Override
    @Transactional
    public void postComment(PostCommentRequest request) {
        commentRepository.save(Comment.builder()
                .message(request.getMessge())
                .sender(currentMember.getMemberPk())
                .notice(noticeRepository.findById(request.getNoticeId()).orElseThrow(NoticeNotFoundException::new))
                .build());
    }

    @Override
    @Transactional
    public void editComment(EditCommentRequest request) {

        Comment comment = noticeRepository.findById(request.getNoticeId()).map(
                notice -> commentRepository.findByIdAndNoticeAndSender(request.getCommentId(), notice, currentMember.getMemberPk())
                        .orElseThrow(CommentNotFoundExcetion::new)
        ).orElseThrow(NoticeNotFoundException::new);
        commentRepository.save(
                comment.edit(request.getMessage())
        );
    }

    @Override
    @Transactional
    public void deleteComment(Long noticeId, Long commentId) {
        commentRepository.delete(
                noticeRepository.findById(noticeId).map(
                        notice -> commentRepository.findByIdAndNoticeAndSender(commentId, notice, currentMember.getMemberPk())
                                .orElseThrow(CommentNotFoundExcetion::new)
                ).orElseThrow(NoticeNotFoundException::new)
        );
    }



    @Override
    public void postReply(ReplyRequest request){
        Comment comment = commentRepository.findById(request.getCommentId()).orElseThrow(CommentNotFoundExcetion::new);

        replyMentRepository.save(
                Replyment.builder()
                        .content(request.getContent())
                        .comment(comment)
                        .build()

        );
        //알림 보내기
        log.info("reply 생성 성공");

    }

    @Override
    public void editReply(ReplyRequest request, Long id){
        Replyment replyment = replyMentRepository.findByIdAndWriter(id, currentMember.getMember()).orElseThrow(ReplyMentException::new);

        replyMentRepository.save(
                replyment.editCont(replyment.getContent())
        );
        //알림 보내기
        log.info("reply 수정 성공");
    }

    @Override
    public void deleteReply(Long id){
        Replyment replyment = replyMentRepository.findByIdAndWriter(id, currentMember.getMember()).orElseThrow(ReplyMentException::new);
        replyMentRepository.delete(replyment);

        log.info("reply 삭제");
    }




    private SoomInfoResponse geneSoomRoomMain(String title, String info, String representativeId, String teacherId, SoomType soomType) {
        String id = UUID.randomUUID().toString();

        SoomRoom soomRoom = SoomRoom.builder()
                .id(id)
                .memberIds(new ArrayList<>())
                .profiles(new ArrayList<>())
                .notices(new ArrayList<>())
                .title(title)
                .info(info)
                .teacherId(teacherId)
                .representativeId(representativeId)
                .soomType(soomType)
                .build().addMember(representativeId);
        String random = UUID.randomUUID().toString().substring(0, 5).toUpperCase(Locale.ROOT);
        soomRepository.save(soomRoom.setJoinCode(random));

        memberRepository.save(currentMember.getMember().joinSoom(soomRoom));

        return soomToSoomInfoResponse(soomRoom);
    }


    private SoomInfoResponse soomToSoomInfoResponse(SoomRoom room) {
        return SoomInfoResponse.builder()
                .id(room.getId())
                .title(room.getTitle())
                .info(room.getId())
                .notices(room.getNotices())
                .memberIds(room.getMemberIds())
                .profiles(room.getProfiles())
                .representativeId(room.getRepresentativeId())
                .soomType(room.getSoomType())
                .teacherId(room.getTeacherId())
                .code(room.getJoinCode())
                .created(room.getCreated())
                .updated(room.getUpdated())
                .build();
    }


    private List<SoomShortResponse> soomRoomToSoomListResopnse(List<SoomRoom> soomRooms) {
        return soomRooms.stream().map(
                soomRoom -> SoomShortResponse.builder()
                        .id(soomRoom.getId())
                        .representativeId(soomRoom.getRepresentativeId())
                        .title(soomRoom.getTitle())
                        .info(soomRoom.getInfo())
                        .memberCounts(soomRoom.getMemberIds().size())
                        .profiles(soomRoom.getProfiles())
                        .soomType(soomRoom.getSoomType())
                        .teacherId(soomRoom.getTeacherId())
                        .updated(soomRoom.getUpdated())
                        .created(soomRoom.getCreated())
                        .build()).collect(Collectors.toList());
    }

    private DetailNoticeResponse noticeToDetailNoticeResponse(Notice notice){
        return DetailNoticeResponse.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .info(notice.getInfo())
                .files(notice.getFileKeys())
                .roomId(notice.getRoom().getId())
                .comments(notice.getComments())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .build();
    }


    private List<NoticeResponse> noticeToNoticeResponse(List<Notice> notices) {
        return notices.stream().map(
                notice -> NoticeResponse.builder()
                        .id(notice.getId())
                        .title(notice.getTitle())
                        .info(notice.getInfo())
                        .files(notice.getFileKeys())
                        .roomId(notice.getRoom().getId())
                        .createdAt(notice.getCreatedAt())
                        .updatedAt(notice.getUpdatedAt())
                        .build()).collect(Collectors.toList());
    }


    private List<String> noticeFileUpload(NoticeFileUploadRequest noticeFileUploadRequest, String soomId) {
        List<String> fileKeys = new ArrayList<>();
        noticeFileUploadRequest.getFiles().forEach(
                file -> {
                    try {
                        fileKeys.add(s3Util.getURL(s3Util.upload(file, "SOOM/NOTICE" + soomId, UUID.randomUUID().toString())).getImageUrl().toString());
                    } catch (IOException e) {
                        throw new UploadException();
                    }
                }
        );
        return fileKeys;
    }


    private SoomRoom noticeAuthenticationCheck(String soomId) {
        String current = currentMember.getMemberPk();
        SoomRoom soomRoom = soomRepository.findById(soomId).orElseThrow(SoomNotFoundException::new);
        if ((!soomRoom.getRepresentativeId().equals(current))||soomRoom.getSoomType().equals(SoomType.ELSE)) {
            try {
                if (!currentMember.getMember().getRole().equals(Role.ROLE_TEACHER)) throw new LowAuthenticationException();
            } catch (NullPointerException e){
                throw new LowAuthenticationException();
            }

        }
        return soomRoom;
    }

    private boolean isIgnoreNotice(String soomRoomId) {
        return currentMember.getMember().getNoticeIgnoreList().contains(soomRoomId);
    }

    private boolean isIgnoreChat(String soomRoomId) {
        return currentMember.getMember().getChatIgnoreList().contains(soomRoomId);
    }

    private void sendExceptNoticeIgnoreList(List<String> receivers, String title, String info) {
        try {
            log.info("sendExceptNoticeIgnoreList: SENDER: " + currentMember.getMemberPk() + "님이 추가하신 파일이 등록되었습니다.");
            fcmService.sendMessageRangeTo(receivers.stream().filter(
                    s -> !memberRepository.findById(s).map(
                            member -> member.getNoticeIgnoreList().contains(s)
                    ).orElseThrow(MemberNotFoundException::new)
            ).collect(Collectors.toList()), title, info);
        } catch (IOException e) {
            throw new DataCannotBringException();
        }
    }

}
