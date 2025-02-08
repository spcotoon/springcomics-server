package com.springcomics.api.admin.adminDto;

import com.querydsl.core.annotations.QueryProjection;
import com.springcomics.api.domain.comic.Genre;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ComicHeadDto {
    private final Long id;
    private final String title;
    private final Genre genre;
    private final String artistPenName;
    private final String artistEmail;
    private final Long amount;
    private final LocalDateTime createdDate;
    private final LocalDateTime lastUploadDate;

    @QueryProjection
    public ComicHeadDto(Long id, String title, Genre genre, String artistPenName, String artistEmail, Long amount, LocalDateTime createdDate, LocalDateTime lastUploadDate) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.artistPenName = artistPenName;
        this.artistEmail = artistEmail;
        this.amount = amount;
        this.createdDate = createdDate;
        this.lastUploadDate = lastUploadDate;
    }
}
