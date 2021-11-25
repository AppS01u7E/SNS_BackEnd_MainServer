package com.jinwoo.snsbackend_mainserver.domain.soom.dao;

import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Member;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.Replyment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReplyMentRepository extends JpaRepository<Replyment, Long> {

    Optional<Replyment> findByIdAndWriter(Long aLong, Member writer);
}
