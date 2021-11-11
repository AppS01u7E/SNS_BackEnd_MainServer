package com.jinwoo.snsbackend_mainserver.domain.schedule.payload;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheReturnResponseDayDto {

    private int grade;
    private int classNum;
    private int totalCount;
    private int day;
    private List<Subject> subjects = new ArrayList<>();

    public static class Subject {
        public String getName() {
            return name;
        }

        public int getPeriod() {
            return period;
        }

        private String name;
        private int period;

        public Subject(String name, int period) {
            this.name = name;
            this.period = period;
        }
    }


    public ScheReturnResponseDayDto(int grade, int classNum, int totalCount, int day, List<Subject> subjects) {
        this.grade = grade;
        this.classNum = classNum;
        this.totalCount = totalCount;
        this.day = day;
        this.subjects = subjects;
    }


}
