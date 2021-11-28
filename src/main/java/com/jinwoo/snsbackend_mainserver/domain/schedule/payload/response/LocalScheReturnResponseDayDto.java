package com.jinwoo.snsbackend_mainserver.domain.schedule.payload.response;

import com.jinwoo.snsbackend_mainserver.domain.schedule.entity.Memo;
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
    public static class Subject {

        private int grade;
        private int classNum;
        private LocalDate date;
        private int period;

        private String name;

        private List<SchoolMemoResponse> memos;


        public Subject setMemos(List<SchoolMemoResponse> memoList){
            this.memos = memoList;
            return this;
        }


        public Subject(String name, int period, int grade, int classNum, LocalDate date, List<SchoolMemoResponse> memos) {
            this.grade = grade;
            this.classNum = classNum;
            this.date = date;
            this.period = period;
            this.name = name;
            this.memos = memos;

        }

        public Subject addMemo(SchoolMemoResponse memo){
            this.memos.add(memo);
            return this;
        }

    }


}
