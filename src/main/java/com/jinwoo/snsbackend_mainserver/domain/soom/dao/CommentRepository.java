package com.jinwoo.snsbackend_mainserver.domain.soom.dao;

import com.jinwoo.snsbackend_mainserver.domain.soom.entity.Comment;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndNoticeaAndSender(Long id, Notice notice, String senderId);

}
