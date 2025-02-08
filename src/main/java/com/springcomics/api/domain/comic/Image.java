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
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comic_body_id")
    private ComicBody comicBody;

    @Builder
    public Image(String imageUrl) {
        this.imageUrl = imageUrl;
    }



    public void addComic(ComicBody comicBody) {
        this.comicBody = comicBody;
    }
}
