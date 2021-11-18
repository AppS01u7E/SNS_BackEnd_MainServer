package com.jinwoo.snsbackend_mainserver.domain.soom.dao;

import com.jinwoo.snsbackend_mainserver.domain.soom.entity.SoomRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<SoomRoom, Long> {
}
