package com.springcomics.api.domain.comic;

import com.springcomics.api.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ComicComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String comment;

    private String userNicknameOfComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comic_body_id")
    private ComicBody comicBody;

    @Builder
    public ComicComment(String comment, String userNicknameOfComment, ComicBody comicBody) {
        this.comment = comment;
        this.userNicknameOfComment = userNicknameOfComment;
        this.comicBody = comicBody;
    }

    public void addComic(ComicBody comicBody) {
        this.comicBody = comicBody;
    }
}
