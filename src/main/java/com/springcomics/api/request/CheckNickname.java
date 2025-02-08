package com.springcomics.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CheckNickname {
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Pattern(regexp = "^[^\\s]+$", message = "닉네임에 공백을 포함할 수 없습니다.")
    @Size(min = 2, max = 6, message = "닉네임은 2자 이상 6자 이하이어야 합니다.")
    private String nickname;
}
