package com.jinwoo.snsbackend_mainserver.domain.soom.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Replyment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;


    @ManyToOne
    @JsonBackReference
    private Comment comment;


    @ManyToOne
    @JsonBackReference
    private Member writer;


    @CreationTimestamp
    private LocalDate createAt;

    public Replyment editCont(String content){
        this.content = content;
        return this;
    }


}
