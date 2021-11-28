package com.jinwoo.snsbackend_mainserver.domain.soom.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

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
    @JsonIgnore
    private Notice notice;

    @OneToMany
    @JsonManagedReference
    private List<Replyment> replymentList;


    public Comment edit(String message){
        this.message = message;
        this.edited = true;
        return this;
    }

    public Comment preDeleteReply(Replyment replyment){
        this.replymentList.remove(replyment);
        return this;
    }

}
