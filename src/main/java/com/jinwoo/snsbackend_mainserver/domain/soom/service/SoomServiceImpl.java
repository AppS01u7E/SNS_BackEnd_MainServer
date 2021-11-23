package com.jinwoo.snsbackend_mainserver.domain.soom.service;

import com.jinwoo.snsbackend_mainserver.domain.auth.dao.MemberRepository;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Role;
import com.jinwoo.snsbackend_mainserver.domain.auth.exception.InvalidCodeException;
import com.jinwoo.snsbackend_mainserver.domain.auth.exception.MemberNotFoundException;
import com.jinwoo.snsbackend_mainserver.domain.soom.dao.CommentRepository;
import com.jinwoo.snsbackend_mainserver.domain.soom.dao.NoticeRepository;
import com.jinwoo.snsbackend_mainserver.domain.soom.dao.SoomRepository;
import com.jinwoo.snsbackend_mainserver.domain.soom.dto.request.*;
import com.jinwoo.snsbackend_mainserver.domain.soom.dto.response.NoticeResponse;
import com.jinwoo.snsbackend_mainserver.domain.soom.dto.response.SoomInfoResponse;
import com.jinwoo.snsbackend_mainserver.domain.soom.dto.response.SoomShortResponse;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.Comment;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.Notice;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.SoomRoom;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.SoomType;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SoomServiceImpl implements SoomService {


    private final CurrentMember currentMember;
    private final S3Util s3Util;
    private final SoomRepository soomRepository;
    private final NoticeRepository noticeRepository;
    private final CommentRepository commentRepository;
    private final FcmServiceImpl fcmService;
    private final MemberRepository memberRepository;


    @Override
    public String teacherGeneSoom(TeacherGeneSoomRequest request) {
        try {
            return geneSoomRoomMain(request.getTitle(), request.getInfo(), request.getRepresentitiveId(), currentMember.getMemberPk(), request.getSoomType());
        } catch (Exception e) {
            throw new SoomException(e.getMessage());
        }
    }


    @Override
    public String geneSoom(GeneSoomRequest request) {
        return geneSoomRoomMain(request.getTitle(), request.getInfo(), currentMember.getMemberPk(), null, SoomType.ELSE);
    }


    @Override
    public void upgradeToClub(String soomId) {
        if (currentMember.getMember().getRole().equals(Role.ROLE_TEACHER)) {
            soomRepository.findById(soomId).map(
                    soomRoom -> soomRepository.save(
                            soomRoom.setRoomType(SoomType.CLUB)
                    )
            ).orElseThrow(SoomNotFoundException::new);
        }
        throw new LowAuthenticationException();
    }

    @Override
    public String postSoomProfile(String soomId, ProfilephotosRequest request) {
        log.info(soomId);
        return soomRepository.findByIdAndRepresentativeId(soomId, currentMember.getMemberPk()).map(
                soomRoom -> {
                    try {
                        return s3Util.upload(request.getProfile(), "SOOM", soomRoom.getId());
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
        if (currentMember.getMember().getRole().equals(Role.ROLE_TEACHER)) soomRepository.findById(request.getSoomId()).orElseThrow(SoomNotFoundException::new).setTeacher(currentMember.getMemberPk());
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
        throw new InvalidCodeException();
    }


    //teacherAuth
    @Override
    public void deleteSoom(String soomId) {
        soomRepository.delete(soomRepository.findById(soomId).orElseThrow(SoomNotFoundException::new));
    }


    //RepStu||teacherAuth
    @Override
    public void editSoom(String soomId, TeacherGeneSoomRequest teacherGeneSoomRequest) {
        noticeAuthenticationCheck(soomId);
        soomRepository.save(
                soomRepository.findById(soomId).map(
                        room -> SoomRoom.builder()
                                .id(room.getId())
                                .title(teacherGeneSoomRequest.getTitle())
                                .info(teacherGeneSoomRequest.getInfo())
                                .teacherId(room.getTeacherId())
                                .representativeId(teacherGeneSoomRequest.getRepresentitiveId())
                                .soomType(teacherGeneSoomRequest.getSoomType())
                                .build()
                ).orElseThrow(SoomNotFoundException::new));
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
    public void deleteFile(Long noticeId, String soomRoomId, String fileKey) {
        Notice notice = noticeRepository.save(noticeRepository.findByIdAndRoom(noticeId, noticeAuthenticationCheck(soomRoomId)).orElseThrow(NoticeNotFoundException::new)
                .deleteFile(fileKey));
        s3Util.delete(fileKey);
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


    private String geneSoomRoomMain(String title, String info, String representativeId, String teacherId, SoomType soomType) {
        SoomRoom soomRoom = SoomRoom.builder()
                .id(UUID.randomUUID().toString())
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

        return random;
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
                .created(room.getCreated())
                .updated(room.getUpdated())
                .build();
    }


    private List<SoomShortResponse> soomRoomToSoomListResopnse(List<SoomRoom> soomRooms) {
        return soomRooms.stream().map(
                soomRoom -> SoomShortResponse.builder()
                        .id(soomRoom.getId())
                        .representativeId(soomRoom.getRepresentativeId())
                        .info(soomRoom.getInfo())
                        .memberCounts(soomRoom.getMemberIds().size())
                        .profiles(soomRoom.getProfiles())
                        .soomType(soomRoom.getSoomType())
                        .teacherId(soomRoom.getTeacherId())
                        .updated(soomRoom.getUpdated())
                        .created(soomRoom.getCreated())
                        .build()).collect(Collectors.toList());
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
                        fileKeys.add(s3Util.upload(file, "SOOM/NOTICE" + soomId, file.getResource().getFilename()));
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
//        if ((!soomRoom.getRepresentativeId().equals(current))||soomRoom.getSoomType().equals(SoomType.ELSE)) {
//            String teacher = null;
//            try {
//                if (!teacher.equals(currentMember.getMemberPk())) throw new LowAuthenticationException();
//            } catch (NullPointerException e){
//                throw new LowAuthenticationException();
//            }
//
//        }
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
            log.info("sendExceptNoticeIgnoreList: SENDER: " + currentMember.getMemberPk() + ", TITLE: " + title);
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
