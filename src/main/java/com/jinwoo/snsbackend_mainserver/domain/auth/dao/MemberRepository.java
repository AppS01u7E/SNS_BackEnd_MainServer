package com.jinwoo.snsbackend_mainserver.domain.auth.dao;

import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
}
