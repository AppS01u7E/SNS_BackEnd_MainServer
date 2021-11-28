package com.jinwoo.snsbackend_mainserver.domain.soom.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jinwoo.snsbackend_mainserver.global.utils.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import javax.persistence.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
public class Notice{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String info;

    @ElementCollection
    private List<String> fileUrls;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.REMOVE)
    @JsonBackReference
    private List<Comment> comments;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "soom_room_id", nullable = false)
    @JsonBackReference
    private SoomRoom room;

    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;


    public Notice(Long id, String title, String info, List<String> fileUrls) {
        this.id = id;
        this.title = title;
        this.info = info;
        this.fileUrls = fileUrls;
    }

    public Notice addFiles(List<String> fileUrls){
        this.fileUrls.addAll(fileUrls);
        return this;
    }

    public Notice deleteFile(String url){
        this.fileUrls.remove(url);
        return this;
    }

    public Notice preDeleteCommnet(Comment comment){
        this.comments.remove(comment);
        return this;
    }


}
