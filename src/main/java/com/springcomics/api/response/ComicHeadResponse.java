package com.springcomics.api.response;

import com.querydsl.core.annotations.QueryProjection;
import com.springcomics.api.domain.comic.Genre;
import lombok.Builder;
import lombok.Data;

@Data
public class ComicHeadResponse {

    private Long id;
    private String title;
    private String synopsis;
    private Genre genre;
    private String thumbnailUrl;
    private String artist;

    @Builder
    @QueryProjection
    public ComicHeadResponse(Long id,String title, String synopsis, Genre genre, String thumbnailUrl, String artist) {
        this.id = id;
        this.title = title;
        this.synopsis = synopsis;
        this.genre = genre;
        this.thumbnailUrl = thumbnailUrl;
        this.artist = artist;
    }


}
