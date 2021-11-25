package com.jinwoo.snsbackend_mainserver.domain.schedule.dao;

import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Member;
import com.jinwoo.snsbackend_mainserver.domain.schedule.entity.Memo;
import com.jinwoo.snsbackend_mainserver.domain.schedule.payload.response.ScheBlockInfo;
import com.jinwoo.snsbackend_mainserver.domain.schedule.entity.ScheduleType;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.SoomRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {

    List<Memo> findAllByGradeAndClassNumAndPeriodAndDate(int grade, int classNum, int period, LocalDate date);

    List<Memo> findAllByWriterAndDateAndPeriod(Member writer, LocalDate date, int period);

    List<Memo> findAllBySoomRoomAndDateAndPeriod(SoomRoom soomRoom, LocalDate date, int period);

    List<Memo> findAllByGradeAndClassNumAndDateBetween(int grade, int classNum, LocalDate startDate, LocalDate endDate);

    List<Memo> findAllByWriterAndDateBetween(Member writer, LocalDate startDate, LocalDate endDate);

    List<Memo> findAllBySoomRoomAndDateBetween(SoomRoom soomRoom, LocalDate startDate, LocalDate endDate);


    Optional<Memo> findByIdAndWriter(Long id, Member writer);
}
