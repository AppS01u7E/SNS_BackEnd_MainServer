package com.jinwoo.snsbackend_mainserver.domain.schedule.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Role;
import com.jinwoo.snsbackend_mainserver.domain.schedule.dao.ScheBlockInfoRepository;
import com.jinwoo.snsbackend_mainserver.domain.schedule.entity.ScheBlockInfo;
import com.jinwoo.snsbackend_mainserver.domain.schedule.entity.ScheduleType;
import com.jinwoo.snsbackend_mainserver.domain.schedule.exception.ScheduleException;
import com.jinwoo.snsbackend_mainserver.domain.schedule.exception.ScheduleInfoNotFoundException;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.request.*;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.response.LocalScheReturnResponseDayDto;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.response.SchoolMonthScheduleResponse;
import com.jinwoo.snsbackend_mainserver.domain.soom.dao.SoomRepository;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheBlockInfoRepository scheBlockInfoRepository;
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
        try {
            return getInfoClsssFromDb(getInfoClsssFromDb(getSchedule(schoolCode, grade, classNum, startDate, endDate))).stream().sorted(
                    Comparator.comparing(
                            localScheReturnResponseDayDto -> localScheReturnResponseDayDto.getDay()
                    )
            ).collect(Collectors.toList());
        } catch (Exception e){
            throw new ScheduleException(e.getMessage());
        }
    }

    public List<LocalScheReturnResponseDayDto> getSoomRageSchdule(int grade, int classNum, int startDate, int endDate){
        String schoolCode = null;
        if (currentMember.getMember().getSchool().equals(com.jinwoo.snsbackend_mainserver.domain.auth.entity.School.DAEDOK)) schoolCode = daedoek;
        try {
            List<LocalScheReturnResponseDayDto> dayDtos = getInfoClsssFromDb(getSchedule(schoolCode, grade, classNum, startDate, endDate));
            return getInfoClubFromDb(dayDtos);
        } catch (Exception e){
            throw new ScheduleException(e.getMessage());
        }
    }


    @Override
    public WriteScheBlockInfoRequest writeInfo(WriteScheBlockInfoRequest request){
        if (!currentMember.getMember().getRole().equals(Role.ROLE_STUDENT)&&request.getType().equals(ScheduleType.ALL)) throw new LowAuthenticationException();
        if (currentMember.getMember().getRole().equals(Role.ROLE_STUDENT)&&!(request.getType().equals(ScheduleType.PERSONAL))) throw new LowAuthenticationException();

        ScheBlockInfo info = scheBlockInfoRepository.save(
                ScheBlockInfo.builder()
                        .writer(currentMember.getMemberPk())
                        .grade(request.getGrade())
                        .classNum(request.getClassNum())
                        .date(request.getDate())
                        .name(request.getName())
                        .period(request.getPeriod())
                        .title(request.getTitle())
                        .info(request.getInfo())
                        .dayScheType(request.getType())
                        .build()
        );

        log.info(info.getInfo());
        return scheBlockToWriteScheReq(info);

    }

    @Override
    public ScheBlockInfo getSepSchduleInfo(int grade, int classNum, int period, int sepDate) {
        String schoolCode = null;
        if (currentMember.getMember().getSchool().equals(com.jinwoo.snsbackend_mainserver.domain.auth.entity.School.DAEDOK)) schoolCode = daedoek;
        try{
            return scheBlockInfoRepository.findByGradeAndClassNumAndDateAndPeriod(grade, classNum, LocalDate.parse(String.valueOf(sepDate), intDateTimeFormatter), period)
                    .orElseThrow(ScheduleInfoNotFoundException::new);
        } catch (Exception e){
            throw new ScheduleException(e.getMessage());
        }
    }

    @Override
    public WriteScheBlockInfoRequest editInfo(WriteScheBlockInfoRequest request){
        ScheBlockInfo info = scheBlockInfoRepository.findByGradeAndClassNumAndDateAndPeriodAndWriter(
                request.getGrade(), request.getClassNum(), request.getDate(),
                request.getPeriod(), currentMember.getMemberPk()
        ).orElseThrow(ScheduleInfoNotFoundException::new);
        if (request.getTitle() != null){
            info.editTitle(request.getTitle());
        }
        if (request.getInfo() != null){
            info.editInfo(request.getInfo());
        }
        return scheBlockToWriteScheReq(info);
    }


    @Override
    public void deleteInfo(IdentifyScheInfoBlockRequest request){
        ScheBlockInfo info = scheBlockInfoRepository.findByGradeAndClassNumAndDateAndPeriodAndWriter(
                request.getGrade(), request.getClassNum(), parseLocalDate(request.getDate()),
                request.getPeriod(), currentMember.getMemberPk()
        ).orElseThrow(ScheduleInfoNotFoundException::new);
        scheBlockInfoRepository.delete(info);
    }


    @Override
    public List<SchoolMonthScheduleResponse> getSchoolList(int yearMonth){
        List<ScheBlockInfo> scheBlockInfoList = scheBlockInfoRepository.findAllByYMonthAndDayScheType(yearMonth, ScheduleType.ALL);

        try {
            List<SchoolMonthScheduleResponse> monthScheduleResponses =
                    getSchoolSchedule(new SchoolScheduleRequest(com.jinwoo.snsbackend_mainserver.domain.auth.entity.School.DAEDOK, yearMonth));
            scheBlockInfoList.forEach(
                    info -> monthScheduleResponses.add(SchoolMonthScheduleResponse.builder()
                            .event(info.getTitle())
                            .day(info.getDate())
                            .build()
            ));

            return monthScheduleResponses.stream().sorted(Comparator.comparing(
                        SchoolMonthScheduleResponse::getDay
                    )).collect(Collectors.toList());
        } catch (IOException e){
            throw new DataCannotBringException();
        }
    }

    @Override
    public List<ScheBlockInfo> getPersonalList(int yeaerMonth) {
        List<ScheBlockInfo> scheBlockInfoList = scheBlockInfoRepository.findAllByWriterAndDayScheType(currentMember.getMemberPk(), ScheduleType.PERSONAL);
        return scheBlockInfoList.stream().sorted(Comparator.comparing(
                info -> info.getDate()
        )).collect(Collectors.toList());
    }


    private List<SchoolMonthScheduleResponse> getSchoolSchedule(SchoolScheduleRequest request) throws IOException {
        String url = schoolScheduleUrl+"?key="+serviceKey;
        URL schoolScheUrl = null;

        if (request.getSchool().equals(com.jinwoo.snsbackend_mainserver.domain.auth.entity.School.DAEDOK)) {
            schoolScheUrl = new URL(url+"&ATPT_OFCDC_SC_CODE=G10&SD_SCHUL_CODE="+daedoek+"&AA_FROM_YMD="+((request.getYearMonth()*100)+1)+"&AA_TO_YMD="+((request.getYearMonth()*100)+30)+"&psize=40&pIndex=1&type=json");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        log.info(schoolScheUrl.toString());
        ScheduleRequest scheduleRequest = objectMapper.readValue(schoolScheUrl, ScheduleRequest.class);

        List<SchoolMonthScheduleResponse> schoolMonthScheduleResponseList = new ArrayList<>();

        scheduleRequest.getSchoolSchedule().get(1).getRow().stream()
                .sorted(Comparator.comparing(ScheduleRequest.SchoolSchedule.Row::getAA_YMD)).collect(Collectors.toList())
                .forEach(
                        row -> schoolMonthScheduleResponseList.add(
                                SchoolMonthScheduleResponse.builder()
                                .day(LocalDate.parse(row.getAA_YMD(), intDateTimeFormatter))
                                .build().addEvent(row.getEVENT_NM())
                        )
                );

        return schoolMonthScheduleResponseList;
    }


    private WriteScheBlockInfoRequest scheBlockToWriteScheReq(ScheBlockInfo info){
        return WriteScheBlockInfoRequest.builder()
                .grade(info.getGrade())
                .classNum(info.getClassNum())
                .date(info.getDate())
                .period(info.getPeriod())
                .title(info.getTitle())
                .info(info.getInfo())
                .build();
    }

    private List<LocalScheReturnResponseDayDto> getInfoClsssFromDb(List<LocalScheReturnResponseDayDto> scheReturnResponseDayDtos){

        List<LocalScheReturnResponseDayDto> scheReturns = new ArrayList<>();

        for (LocalScheReturnResponseDayDto day:scheReturnResponseDayDtos){

            day.setSubjects(day.getSubjects().stream().map(
                    subject -> (scheBlockInfoRepository.findByGradeAndClassNumAndDateAndPeriod(day.getGrade(), day.getClassNum(),
                                    subject.getDate(), subject.getPeriod())
                            .orElse(new ScheBlockInfo(subject.getName(), subject.getPeriod(), subject.getGrade(),
                                    subject.getClassNum(), subject.getDate(), day.getDay()/100))).toSubject()
            ).sorted(Comparator.comparing(
                    LocalScheReturnResponseDayDto.Subject::getPeriod
            )).collect(Collectors.toList()));
            scheReturns.add(day);
        }
        return scheReturns;
    }

    private List<LocalScheReturnResponseDayDto> getInfoClubFromDb(String soomId, List<LocalScheReturnResponseDayDto> scheReturnResponseDayDtos){

        List<LocalScheReturnResponseDayDto> scheReturns = new ArrayList<>();

        for (LocalScheReturnResponseDayDto day:scheReturnResponseDayDtos){

            day.setSubjects(day.getSubjects().stream().map(
                    subject -> (scheBlockInfoRepository.findBySoomRoomAndDateAndPeriod(soomRepository.findById(soomId).orElseThrow(SoomNotFoundException::new),
                                    subject.getDate(), subject.getPeriod())
                            .orElse(new ScheBlockInfo(subject.getName(), subject.getPeriod(), subject.getGrade(),
                                    subject.getClassNum(), subject.getDate(), day.getDay()/100))).toSubject()
            ).sorted(Comparator.comparing(
                    LocalScheReturnResponseDayDto.Subject::getPeriod
            )).collect(Collectors.toList()));
            scheReturns.add(day);
        }
        return scheReturns;
    }






    private List<LocalScheReturnResponseDayDto> getSchedule(String schoolCode, int grade, int classNum, int startDate, int endDate){
        try{
            School school = new School(serviceKey);
            return school.getSchoolSchedule(schoolCode, grade, classNum, startDate, endDate)
                    .stream().map(
                            scheReturnResponseDayDto -> new LocalScheReturnResponseDayDto(scheReturnResponseDayDto.getGrade(),
                                    scheReturnResponseDayDto.getClassNum(), scheReturnResponseDayDto.getTotalCount(), scheReturnResponseDayDto.getDay(), scheReturnResponseDayDto.getSubjects().stream()
                                    .map(
                                            subject -> new LocalScheReturnResponseDayDto.Subject(subject.getName(), subject.getPeriod(), scheReturnResponseDayDto.getGrade(),
                                                    scheReturnResponseDayDto.getClassNum(), parseLocalDate(scheReturnResponseDayDto.getDay()))
                                    ).collect(Collectors.toList()))).collect(Collectors.toList());
        } catch (IOException e){
            return null;
        }          catch (Exception e){
            throw new ScheduleException(e.getMessage());
        }
    }

    private LocalDate parseLocalDate(int date){
        return LocalDate.parse(String.valueOf(date), intDateTimeFormatter);
    }

}
