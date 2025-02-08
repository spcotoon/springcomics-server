package com.springcomics.api.admin.adminDto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class ComicBodyDto {
    private final Long id;
    private final String title;
    private final Long amount;

    @QueryProjection
    public ComicBodyDto(Long id, String title, Long amount) {
        this.id = id;
        this.title = title;
        this.amount = amount;
    }
}
