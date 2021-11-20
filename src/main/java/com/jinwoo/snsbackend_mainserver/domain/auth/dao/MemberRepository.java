package com.jinwoo.snsbackend_mainserver.domain.auth.dao;

import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Member;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.School;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.SoomRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.config.ScheduledTaskHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, String> {
    //callingService
    List<Member> findAllBySchool(School school);
    Optional<Member> findBySchoolAndId(School school, String id);
    List<Member> findAllBySchoolAndGradeAndClassNum(School school, int grade, int classNum);
    List<Member> findAllBySchoolAndSoomRoomsContaining(School school, SoomRoom club);
    List<Member> findAllBySchoolAndGrade(School school, int grade);
}
