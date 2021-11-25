package com.jinwoo.snsbackend_mainserver.domain.soom.dto.response;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.Comment;
import com.jinwoo.snsbackend_mainserver.domain.soom.entity.SoomRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.ElementCollection;
import javax.persistence.ManyToOne;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeResponse {

    private Long id;

    private String title;
    private String info;

    private List<String> files;

    private String roomId;

    private LocalDate createdAt;

    private LocalDate updatedAt;
}
