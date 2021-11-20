package com.jinwoo.snsbackend_mainserver.domain.soom.dao;

import com.jinwoo.snsbackend_mainserver.domain.soom.entity.Notice;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.SoomRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Page<Notice> findAllByRoomOrderByCreatedAtDesc(SoomRoom room, Pageable pageable);

    Optional<Notice> findByIdAndRoom(Long id, SoomRoom room);
}
