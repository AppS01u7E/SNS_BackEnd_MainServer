package com.jinwoo.snsbackend_mainserver.domain.schedule.payload.response;

import com.jinwoo.snsbackend_mainserver.domain.schedule.entity.ScheduleType;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocalScheReturnResponseDayDto {

    private int grade;
    private int classNum;
    private int totalCount;
    private int day;


    public List<Subject> getSubjects() {
        return this.subjects;
    }

    private List<Subject> subjects = new ArrayList<>();


    @Getter
    @Setter
    public static class Subject {

        private int grade;
        private int classNum;
        private LocalDate date;
        private int period;

        private String name;

        private String title;
        private String titleInfo;
        private ScheduleType scheduleType;

        private List<InfoMemo> infoMemos;


        public Subject(String name, int period, int grade, int classNum, LocalDate date) {
            this.grade = grade;
            this.classNum = classNum;
            this.date = date;
            this.period = period;
            this.name = name;
        }

        public Subject(String name, int period, int grade, int classNum, LocalDate date, List<InfoMemo> info, ScheduleType dayScheType) {
            this.name = name;
            this.period = period;
            this.grade = grade;
            this.classNum = classNum;
            this.date = date;
            this.infoMemos = info;
            this.scheduleType = dayScheType;
        }

        public Subject setTitleInfo(String titleInfo) {
            this.titleInfo = titleInfo;
            return this;
        }

        public Subject(String name, int period, int grade, int classNumm, LocalDate date, String title, String titleInfo, ScheduleType type) {
            this.name = name;
            this.period = period;
            this.grade = grade;
            this.classNum = classNumm;
            this.date = date;
            this.scheduleType = type;
            this.title = title;
            this.titleInfo = titleInfo;

        }

    }


}
