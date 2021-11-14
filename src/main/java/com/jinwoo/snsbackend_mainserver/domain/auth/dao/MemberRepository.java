package com.jinwoo.snsbackend_mainserver.domain.auth.dao;

import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Member;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    //callingService
    List<Member> findAllBySchool(School school);
    Optional<Member> findBySchoolAndId(School school, String id);
}
