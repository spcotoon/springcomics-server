package com.springcomics.api.admin.adminDto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArtistDto {

    private final Long id;
    private final String email;
    private final String penName;
    private final LocalDateTime createdDate;
    private final Long seriesAmount;

    @Builder
    @QueryProjection
    public ArtistDto(Long id, String email, String penName, LocalDateTime createdDate, Long seriesAmount) {
        this.id = id;
        this.email = email;
        this.penName = penName;
        this.createdDate = createdDate;
        this.seriesAmount = seriesAmount;
    }
}
