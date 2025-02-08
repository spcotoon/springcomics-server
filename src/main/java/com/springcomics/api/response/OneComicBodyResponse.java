package com.springcomics.api.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OneComicBodyResponse {
    private Long id;
    private String title;
    private String authorComment;
    private String thumbnail;
    private List<String> imageUrls;
    private LocalDateTime uploadDate;
    private Long no;

    @QueryProjection
    public OneComicBodyResponse(Long id, String title, String authorComment, String thumbnail, List<String> imageUrls, LocalDateTime uploadDate, Long no) {
        this.id = id;
        this.title = title;
        this.authorComment = authorComment;
        this.thumbnail = thumbnail;
        this.imageUrls = imageUrls;
        this.uploadDate = uploadDate;
        this.no = no;
    }
}
