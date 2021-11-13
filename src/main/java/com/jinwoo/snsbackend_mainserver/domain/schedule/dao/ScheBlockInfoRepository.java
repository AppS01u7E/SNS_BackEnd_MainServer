package com.jinwoo.snsbackend_mainserver.domain.schedule.dao;

import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Member;
import com.jinwoo.snsbackend_mainserver.domain.schedule.entity.ScheBlockInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ScheBlockInfoRepository extends JpaRepository<ScheBlockInfo, Long> {
    Optional<ScheBlockInfo> findByGradeAndClassNumAndDateAndPeriod(int grade, int classNum, LocalDate date,
                                                                   int period);
    Optional<ScheBlockInfo> findByGradeAndClassNumAndDateAndPeriodAndWriter(int grade, int classNum, LocalDate date,
                                                                            int period, String writer);

}
