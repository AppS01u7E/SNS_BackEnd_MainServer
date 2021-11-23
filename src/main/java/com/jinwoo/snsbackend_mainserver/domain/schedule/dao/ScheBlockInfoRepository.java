package com.jinwoo.snsbackend_mainserver.domain.schedule.dao;

import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Member;
import com.jinwoo.snsbackend_mainserver.domain.schedule.entity.ScheBlockInfo;
import com.jinwoo.snsbackend_mainserver.domain.schedule.entity.ScheduleType;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.SoomRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheBlockInfoRepository extends JpaRepository<ScheBlockInfo, Long> {
    Optional<ScheBlockInfo> findByGradeAndClassNumAndDateAndPeriod(int grade, int classNum, LocalDate date,
                                                                   int period);

    Optional<ScheBlockInfo> findBySoomRoomAndDateAndPeriod(SoomRoom soomRoom, LocalDate date,
                                                       int period);


    Optional<ScheBlockInfo> findByGradeAndClassNumAndDateAndPeriodAndWriter(int grade, int classNum, LocalDate date,
                                                                            int period, String writer);

    List<ScheBlockInfo> findAllByYMonthAndDayScheType(int yearMonth, ScheduleType type);

    List<ScheBlockInfo> findAllByWriterAndDayScheType(String writerId, ScheduleType type);


}
