package com.springcomics.api.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentPost {
    @Size(max = 200, message = "200자 이내로 입력해주세요.")
    private final String comment;
}
