package com.springcomics.api.domain.comic;

import com.springcomics.api.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ComicBody extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comic_body_id")
    private Long id;

    private Long no;

    private String title;

    private String thumbnail;

    private String authorComment;

    @Enumerated(EnumType.STRING)
    private ComicStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comic_head_id")
    private ComicHead comicHead;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comicBody")
    private List<Image> images = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comicBody")
    private List<ComicComment> comments = new ArrayList<>();

    @Builder
    public ComicBody(String title, String thumbnail, String authorComment, List<Image> images, ComicHead comicHead, Long no) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.authorComment = authorComment;
        this.status = ComicStatus.PENDING;
        this.comicHead = comicHead;
        this.no = no;
        this.images = images != null ? images : new ArrayList<>();
        this.images.forEach(image -> image.addComic(this));
    }

    public void addComment(ComicComment comment) {
        comments.add(comment);
        comment.addComic(this);
    }

    public void removeComment(ComicComment comment) {
        comments.remove(comment);
        comment.addComic(null);
    }

    public void addImage(Image image) {
        images.add(image);
        image.addComic(this);
    }

    public void removeImage(Image image) {
        images.remove(image);
        image.addComic(null);
    }

    public void comicPublish() {
        this.status = ComicStatus.PUBLISHED;
    }

    public void revertToPending() {
        this.status = ComicStatus.PENDING;
    }

    public void addComicHead(ComicHead comicHead) {
        this.comicHead = comicHead;
        comicHead.addComicBody(this);
    }
}
