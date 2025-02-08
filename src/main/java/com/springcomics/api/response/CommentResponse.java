package com.springcomics.api.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse {
    private String comment;
    private LocalDateTime postDate;
    private String postUserNickname;
    private String postUserEmail;

    @Builder
    @QueryProjection
    public CommentResponse(String comment, LocalDateTime postDate, String postUserNickname, String postUserEmail) {
        this.comment = comment;
        this.postDate = postDate;
        this.postUserNickname = postUserNickname;
        this.postUserEmail = postUserEmail;
    }

}
