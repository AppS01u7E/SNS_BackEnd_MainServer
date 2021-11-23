package com.jinwoo.snsbackend_mainserver.domain.soom.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;

    private String message;

    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updateAt;

    private boolean edited = false;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    public Comment edit(String message){
        this.message = message;
        this.edited = true;
        return this;
    }

}
