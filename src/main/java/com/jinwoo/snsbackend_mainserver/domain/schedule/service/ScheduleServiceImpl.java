package com.jinwoo.snsbackend_mainserver.domain.schedule.service;

import com.jinwoo.snsbackend_mainserver.domain.schedule.dao.ScheBlockInfoRepository;
import com.jinwoo.snsbackend_mainserver.domain.schedule.entity.ScheBlockInfo;
import com.jinwoo.snsbackend_mainserver.domain.schedule.exception.ScheduleException;
import com.jinwoo.snsbackend_mainserver.domain.schedule.exception.ScheduleInfoNotFoundException;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.request.IdentifyScheInfoBlockRequest;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.request.WriteScheBlockInfoRequest;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.response.LocalScheReturnResponseDayDto;
import com.jinwoo.snsbackend_mainserver.global.exception.DataCannotBringException;
import com.jinwoo.snsbackend_mainserver.global.utils.CurrentMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neiseApi.School;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheBlockInfoRepository scheBlockInfoRepository;
    private final CurrentMember currentMember;
    private final DateTimeFormatter intDateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");


    @Value("${neis.serviceKey}")
    private String serviceKey;
    private final School school = new School(serviceKey);


    @Override
    public List<LocalScheReturnResponseDayDto> getRangeSchedule(int grade, int classNum, int startDate, int endDate, String schoolCode){
        try {
            return getInfoFromDb(getSchedule(schoolCode, grade, classNum, startDate, endDate));
        } catch (Exception e){
            throw new ScheduleException(e.getMessage());
        }
    }

    @Override
    public WriteScheBlockInfoRequest writeInfo(WriteScheBlockInfoRequest request){
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
                        .type(request.getType())
                        .build()
        );
        log.info(info.getInfo());

        return scheBlockToWriteScheReq(info);

    }

    @Override
    public ScheBlockInfo getSepSchduleInfo(int grade, int classNum, String schoolCode, int period, int sepDate) {
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

    private List<LocalScheReturnResponseDayDto> getInfoFromDb(List<LocalScheReturnResponseDayDto> scheReturnResponseDayDtos){

        List<LocalScheReturnResponseDayDto> scheReturns = new ArrayList<>();

        for (LocalScheReturnResponseDayDto day:scheReturnResponseDayDtos){

            day.setSubjects(day.getSubjects().stream().map(
                    subject -> (scheBlockInfoRepository.findByGradeAndClassNumAndDateAndPeriod(day.getGrade(), day.getClassNum(),
                                    subject.getDate(), subject.getPeriod())
                            .orElse(new ScheBlockInfo(subject.getName(), subject.getPeriod(), subject.getGrade(), subject.getClassNum(), subject.getDate())))
                            .toSubject()
            ).collect(Collectors.toList()));
            scheReturns.add(day);
        }
        return scheReturns;
    }

    private List<LocalScheReturnResponseDayDto> getSchedule(String schoolCode, int grade, int classNum, int startDate, int endDate){
        try{
            return school.getSchoolSchedule(schoolCode, grade, classNum, startDate, endDate)
                    .stream().map(
                            scheReturnResponseDayDto -> new LocalScheReturnResponseDayDto(scheReturnResponseDayDto.getGrade(),
                                    scheReturnResponseDayDto.getClassNum(), scheReturnResponseDayDto.getTotalCount(), scheReturnResponseDayDto.getDay(), scheReturnResponseDayDto.getSubjects().stream()
                                    .map(
                                            subject -> new LocalScheReturnResponseDayDto.Subject(subject.getName(), subject.getPeriod(), scheReturnResponseDayDto.getGrade(),
                                                    scheReturnResponseDayDto.getClassNum(), parseLocalDate(scheReturnResponseDayDto.getDay()))
                                    ).collect(Collectors.toList()))).collect(Collectors.toList());
        } catch (IOException e){
            throw new DataCannotBringException();
        } catch (Exception e){
            throw new ScheduleException(e.getMessage());
        }
    }

    private LocalDate parseLocalDate(int date){
        return LocalDate.parse(String.valueOf(date), intDateTimeFormatter);
    }

}
