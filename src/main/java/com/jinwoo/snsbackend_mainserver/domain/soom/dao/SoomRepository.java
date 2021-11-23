package com.jinwoo.snsbackend_mainserver.domain.soom.dao;

import com.jinwoo.snsbackend_mainserver.domain.soom.entity.SoomRoom;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SoomRepository extends JpaRepository<SoomRoom, String> {


    List<SoomRoom> findAllByMemberIdsContains(String memberId);

    Page<SoomRoom> findAllByTitleContains(String title, Pageable pageable);

    @NotNull
    @Override
    Page<SoomRoom> findAll(@NotNull Pageable pageable);

    Optional<SoomRoom> findByIdAndMemberIdsContains(String soomId, String memberId);

    Optional<SoomRoom> findByIdAndRepresentativeId(String soomId, String represendId);


}
