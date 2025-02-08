package com.springcomics.api.request;

import lombok.*;

import java.util.List;

@Data
public class ComicUpload {

    private String title;
    private String authorComment;
    private String thumbnail;
    private List<String> imageUrls;

    @Builder
    public ComicUpload(String title, String thumbnail, String authorComment,List<String> imageUrls) {
        this.title = title;
        this.authorComment = authorComment;
        this.thumbnail = thumbnail;
        this.imageUrls = imageUrls;
    }
}
