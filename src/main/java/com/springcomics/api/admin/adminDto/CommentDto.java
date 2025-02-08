package com.springcomics.api.admin.adminDto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private final Long id;
    private final String writer;
    private final String content;
    private LocalDateTime createdDate;

    @Builder
    @QueryProjection
    public CommentDto(Long id, String writer, String content, LocalDateTime createdDate) {
        this.id = id;
        this.writer = writer;
        this.content = content;
        this.createdDate = createdDate;
    }
}
