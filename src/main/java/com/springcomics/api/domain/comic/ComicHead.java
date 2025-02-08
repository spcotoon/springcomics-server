package com.springcomics.api.domain.comic;

import com.springcomics.api.domain.BaseEntity;
import com.springcomics.api.domain.member.Artist;
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
public class ComicHead extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comic_head_id")
    private Long id;

    private String title;
    private String synopsis;
    @Enumerated(EnumType.STRING)
    private Genre genre;
    private String thumbnailUrl;
    private Boolean approval;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comicHead")
    private List<ComicBody> comicBodies = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @Builder
    public ComicHead(String title, String synopsis, Genre genre, String thumbnailUrl, Artist artist) {
        this.title = title;
        this.synopsis = synopsis;
        this.genre = genre;
        this.approval = false;
        this.thumbnailUrl = thumbnailUrl;
        this.artist = artist;
    }

    public void approveComicSerialization() {
        this.approval = true;
    }

    public void addComicBody(ComicBody comicBody) {
        this.comicBodies.add(comicBody);
        comicBody.addComicHead(this);
        }

    public void changeGenre(Genre genre) {
        this.genre = genre;
    }
}
