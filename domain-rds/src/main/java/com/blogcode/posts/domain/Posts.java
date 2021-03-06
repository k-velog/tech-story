package com.blogcode.posts.domain;

import com.blogcode.base.BaseEntity;
import com.blogcode.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Setter @Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
public class Posts extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(insertable = false, updatable = false, length = 100)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(length = 50)
    private Long writerId;

    @Column(length = 50)
    private String writerEmail;

    @Column(length = 50)
    private String writerName;

    @Column(insertable = false)
    @ColumnDefault("0")
    private Long views;

    @Column(insertable = false)
    @ColumnDefault("0")
    private Long likes;

    @Column(length = 200)
    private String thumbnailPath;

    @ColumnDefault("0")
    private Long countScripting;

    @ColumnDefault("0")
    private String tempSaveStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private PostType dType;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;

    @JsonIgnore
    @OneToMany(mappedBy = "posts")
    private List<Reply> replyList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "posts")
    private List<HashTag> hashTags = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "posts")
    private List<Likes> likesList = new ArrayList<>();

    @PrePersist
    public void update(){
        this.createId = member.getId();
        this.modifyId = member.getId();

        if(this.id == null){
            this.likes = 0L;
            this.views = 0L;
        }
    }

    public void setMemberData(Member member) {
        this.member = member;
        this.writerId = member.getId();
        this.writerEmail = member.getEmail();
        this.writerName = member.getName();
    }
}
