package com.springcomics.api.admin.adminDto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberDto {
    private Long id;
    private String email;
    private String nickName;
    private LocalDateTime createdDate;

    @Builder
    public MemberDto(Long id, String email, String nickName, LocalDateTime createdDate) {
        this.id = id;
        this.email = email;
        this.nickName = nickName;
        this.createdDate = createdDate;
    }
}
