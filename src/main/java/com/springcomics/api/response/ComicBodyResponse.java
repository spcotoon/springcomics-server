package com.springcomics.api.response;

import com.querydsl.core.annotations.QueryProjection;
import com.springcomics.api.domain.comic.Genre;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ComicBodyResponse {

    private Long headId;
    private String artist;
    private String headTitle;
    private String headSynopsis;
    private Genre genre;
    private String headThumbnail;

    private Long id;
    private String title;
    private String authorComment;
    private String thumbnail;
    private List<String> imageUrls;
    private LocalDateTime uploadDate;
    private Long no;

    @Builder
    @QueryProjection
    public ComicBodyResponse(Long headId, String artist, String headTitle, String headSynopsis, Genre genre, String headThumbnail, Long id, String title, String authorComment, String thumbnail, List<String> imageUrls, LocalDateTime uploadDate,Long no) {
        this.headId = headId;
        this.artist = artist;
        this.headTitle = headTitle;
        this.headSynopsis = headSynopsis;
        this.genre = genre;
        this.headThumbnail = headThumbnail;
        this.id = id;
        this.title = title;
        this.authorComment = authorComment;
        this.thumbnail = thumbnail;
        this.imageUrls = imageUrls;
        this.uploadDate = uploadDate;
        this.no = no;
    }
}
