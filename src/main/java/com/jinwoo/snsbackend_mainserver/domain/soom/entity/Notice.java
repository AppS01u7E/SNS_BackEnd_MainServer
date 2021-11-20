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
    private List<String> fileKeys = new ArrayList<>();

    @OneToMany
    @JsonBackReference
    @JoinColumn(name = "comment_id")
    private List<Comment> comments;


    @ManyToOne
    @JsonBackReference
    private SoomRoom room;

    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;


    public Notice(Long id, String title, String info, List<String> fileKeys) {
        this.id = id;
        this.title = title;
        this.info = info;
        this.fileKeys = fileKeys;
    }

    public Notice addFiles(List<String> keys){
        this.fileKeys.addAll(keys);
        return this;
    }

    public Notice deleteFile(String key){
        this.fileKeys.remove(key);
        return this;
    }


}
