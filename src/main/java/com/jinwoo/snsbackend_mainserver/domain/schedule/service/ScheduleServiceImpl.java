package com.jinwoo.snsbackend_mainserver.domain.schedule.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Member;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Role;
import com.jinwoo.snsbackend_mainserver.domain.schedule.dao.MemoRepository;
import com.jinwoo.snsbackend_mainserver.domain.schedule.entity.Memo;
import com.jinwoo.snsbackend_mainserver.domain.schedule.exception.UnformattedDateException;
import com.jinwoo.snsbackend_mainserver.domain.schedule.entity.ScheduleType;
import com.jinwoo.snsbackend_mainserver.domain.schedule.exception.MemoNotFoundException;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.request.*;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.response.LocalScheReturnResponseDayDto;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.response.SchoolMemoResponse;
import com.jinwoo.snsbackend_mainserver.domain.soom.dao.SoomRepository;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.SoomRoom;
import com.jinwoo.snsbackend_mainserver.domain.soom.exception.SoomNotFoundException;
import com.jinwoo.snsbackend_mainserver.global.exception.DataCannotBringException;
import com.jinwoo.snsbackend_mainserver.global.exception.LowAuthenticationException;
import com.jinwoo.snsbackend_mainserver.global.utils.CurrentMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neiseApi.School;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final MemoRepository memoRepository;
    private final CurrentMember currentMember;
    private final DateTimeFormatter intDateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final SoomRepository soomRepository;

    @Value("${school.daedoek}")
    private String daedoek;



    @Value("${neis.serviceKey}")
    private String serviceKey;

    @Value("${neis.schoolschedule}")
    private String schoolScheduleUrl;




    @Override
    public List<LocalScheReturnResponseDayDto> getRangeSchedule(int grade, int classNum, int startDate, int endDate){
        String schoolCode = null;
        if (currentMember.getMember().getSchool().equals(com.jinwoo.snsbackend_mainserver.domain.auth.entity.School.DAEDOK)) schoolCode = daedoek;

        return getSchedule(schoolCode, grade, classNum, startDate, endDate);

    }



    @Override
    public void writeMemoInfo(WriteMemoInfoRequest r) {
        if (currentMember.getMember().getRole().equals(Role.ROLE_STUDENT)) throw new LowAuthenticationException();

        Memo memo = buildMember(r.getTitle(), r.getInfo(), r.getType(), r.getGrade(), r.getClassNum(),
                r.getPeriod(), r.getDate(), null);

       memoRepository.save(memo);

        log.info(currentMember.getMemberPk()+"선생님께서 메모를 추가하셨습니다.    :"
                + memo.getTitle());

    }

    @Override
    public void writeSoomMemoInfo(SoomMemoInfoRequest r) {
        SoomRoom soomRoom = soomRepository.findById(r.getSoomRoomId()).orElseThrow(SoomNotFoundException::new);
        if (!soomRoom.getRepresentativeId()
                .equals(currentMember.getMemberPk())&&!(soomRoom.getTeacherId().equals(currentMember.getMemberPk()))) throw new LowAuthenticationException();

        Memo memo = buildMember(r.getTitle(), r.getInfo(), ScheduleType.SOOM,
                0, 0,
                r.getPeriod(), r.getDate(), soomRoom);

        memoRepository.save(memo);
        log.info(currentMember.getMember().getName() + "님께서 동아리에 공지를 등록하였습니다.    :"
                + memo.getTitle());

    }

    @Override
    public void writePersonalMemoInfo(WritePersonalMemoInfoRequest r) {
        memoRepository.save(
                buildMember(r.getTitle(), r.getInfo(), ScheduleType.PERSONAL,
                0, 0,
                r.getPeriod(), r.getDate(), null)
        );
        log.info(currentMember.getMemberPk()+"님께서 개인 일정을 등록하셨습니다.    :"+r.getTitle());
    }

    private Memo buildMember(String title, String info, ScheduleType type,
                               int grade, int classNum, int period, LocalDate date, SoomRoom room){
        return Memo.builder()
                .title(title)
                .info(info)
                .scheduleType(type)
                .writer(currentMember.getMember())
                .grade(grade)
                .classNum(classNum)
                .period(period)
                .date(date)
                .soomRoom(room)
                .build();
    }



    @Override
    public void editMemoInfo(Long memoId, String title, String info){
        Memo memo = memoRepository.findByIdAndWriter(memoId, currentMember.getMember()).orElseThrow(MemoNotFoundException::new);

        memo.setTitle(title);
        memo.setInfo(info);
        memoRepository.save(memo);
    }


    @Override
    public void deleteMemoInfo(Long infoId){
        Memo memo = memoRepository.findByIdAndWriter(infoId, currentMember.getMember()).orElseThrow(MemoNotFoundException::new);
        memoRepository.delete(memo.preDelete());
    }





    @Override
    public List<SchoolMemoResponse> getSchoolList(int year, int month, com.jinwoo.snsbackend_mainserver.domain.auth.entity.School school){

        Member member = currentMember.getMember();
        Calendar cal = Calendar.getInstance();
        cal.set(year%1000, month-1, 1);
        if (!(month>=1)||!(month<=12)) throw new UnformattedDateException();
        LocalDate startDate = LocalDate.of(year, month, cal.getMinimum(Calendar.DAY_OF_MONTH));
        LocalDate endDate = LocalDate.of(year, month, cal.getActualMaximum(Calendar.DAY_OF_MONTH));



        List<Memo> memoList = memoRepository.findAllByGradeAndClassNumAndDateBetween(member.getGrade(), member.getClassNum(), startDate, endDate);
        member.getSoomRooms().forEach(
                soomRoom -> {
                    memoList.addAll(memoRepository.findAllBySoomRoomAndDateBetween(soomRoom, startDate, endDate));
                }
        );
        memoList.addAll(memoRepository.findAllByWriterAndDateBetween(member, startDate, endDate));

        List<SchoolMemoResponse> memoResponses = memoToSchoolMemoResponse(
                memoList.stream().sorted(Comparator.comparing(
                Memo::getDate
        )).collect(Collectors.toList()));
        log.info(String.valueOf(cal.getMinimum(Calendar.DAY_OF_MONTH)));
        memoResponses.addAll(getSchoolSchedule(school, Integer.parseInt(String.valueOf(year)+String.valueOf(month)+"01"), Integer.parseInt(String.valueOf(year)+String.valueOf(month)+String.valueOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH)))));

        return memoResponses.stream().sorted(
                Comparator.comparing(o -> o.getDate().toString())
        ).collect(Collectors.toList());
    }

    private List<SchoolMemoResponse> memoToSchoolMemoResponse(List<Memo> memoList){
        List<SchoolMemoResponse> schoolMemoResponseList = new ArrayList<>();
        memoList.forEach(
                memo -> schoolMemoResponseList.add(
                        SchoolMemoResponse.builder()
                                .id(memo.getId())
                                .title(memo.getTitle())
                                .info(memo.getInfo())
                                .scheduleType(memo.getScheduleType())
                                .writerId(memo.getWriter().getId())
                                .date(memo.getDate())
                                .soomRoomId(memo.getSoomRoom().getId())
                                .soomRoomName(memo.getSoomRoom().getTitle())
                                .build()
                )
        );
        return schoolMemoResponseList;
    }




    private List<SchoolMemoResponse> getSchoolSchedule(com.jinwoo.snsbackend_mainserver.domain.auth.entity.School school, int from, int to) {
        String url = schoolScheduleUrl+"?key="+serviceKey;
        URL schoolScheUrl = null;
        ScheduleRequest scheduleRequest = null;

        try {
            if (school.equals(com.jinwoo.snsbackend_mainserver.domain.auth.entity.School.DAEDOK)) {
                schoolScheUrl = new URL(url+"&ATPT_OFCDC_SC_CODE=G10&SD_SCHUL_CODE="+daedoek+"&AA_FROM_YMD="+(from)+"&AA_TO_YMD="+(to)+"&psize=40&pIndex=1&type=json");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            log.info(schoolScheUrl.toString());

            scheduleRequest = objectMapper.readValue(schoolScheUrl, ScheduleRequest.class);
        } catch (IOException e){
            throw new DataCannotBringException();
        }
        List<SchoolMemoResponse> schoolMonthScheduleResponseList = new ArrayList<>();

        scheduleRequest.getSchoolSchedule().get(1).getRow().stream()
                .sorted(Comparator.comparing(ScheduleRequest.SchoolSchedule.Row::getAA_YMD)).collect(Collectors.toList())
                .forEach(
                        row -> schoolMonthScheduleResponseList.add(
                                SchoolMemoResponse.builder()
                                        .title(row.getEVENT_NM())
                                        .info(null)
                                        .scheduleType(ScheduleType.SCHOOL)
                                        .writerId(null)
                                        .date(LocalDate.parse(row.getAA_YMD(), intDateTimeFormatter))
                                        .soomRoomName(null)
                                        .soomRoomId(null)
                                        .build()
                        )
                );

        return schoolMonthScheduleResponseList;
    }




    private List<LocalScheReturnResponseDayDto> getSchedule(String schoolCode, int grade, int classNum, int startDate, int endDate){
        try{
            School school = new School(serviceKey);

            return school.getSchoolSchedule(schoolCode, grade, classNum, startDate, endDate)
                    .stream().map(
                            scheReturnResponseDayDto -> new LocalScheReturnResponseDayDto(scheReturnResponseDayDto.getGrade(),
                                    scheReturnResponseDayDto.getClassNum(), scheReturnResponseDayDto.getTotalCount(), scheReturnResponseDayDto.getDay(), scheReturnResponseDayDto.getSubjects().stream()
                                    .map(
                                            subject -> {
                                                log.info(String.valueOf(scheReturnResponseDayDto.getDay()));
                                                List<Memo> memoList = memoRepository.findAllByGradeAndClassNumAndPeriodAndDate(scheReturnResponseDayDto.getGrade(), scheReturnResponseDayDto.getClassNum(),
                                                        subject.getPeriod(), parseLocalDate(scheReturnResponseDayDto.getDay()));

                                                memoList.addAll(memoRepository.findAllByWriterAndDateAndPeriod(currentMember.getMember(),
                                                        parseLocalDate(scheReturnResponseDayDto.getDay()),
                                                        subject.getPeriod()));
                                                currentMember.getMember().getSoomRooms().forEach(
                                                        soomRoom -> memoList.addAll(memoRepository.findAllBySoomRoomAndDateAndPeriod(soomRoom, parseLocalDate(scheReturnResponseDayDto.getDay()),
                                                                subject.getPeriod())));
                                                return new LocalScheReturnResponseDayDto.Subject(subject.getName(), subject.getPeriod(), scheReturnResponseDayDto.getGrade(), currentMember.getMember().getClassNum(),
                                                        parseLocalDate(scheReturnResponseDayDto.getDay()), memoToSchoolMemoResponse(memoList));
                                            }
                                    ).sorted(Comparator.comparing(
                                            LocalScheReturnResponseDayDto.Subject::getPeriod
                                    )).collect(Collectors.toList())))
                    .sorted(Comparator.comparing(
                            LocalScheReturnResponseDayDto::getDay
                    ))
                    .collect(Collectors.toList());
        } catch (IOException e){
            return null;
        }
    }

    private LocalDate parseLocalDate(int date){
        return LocalDate.parse(String.valueOf(date), intDateTimeFormatter);
    }

}
