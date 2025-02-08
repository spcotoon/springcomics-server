package com.springcomics.api.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
public class UserSignup {

        @NotBlank(message = "유효한 이메일을 입력해주세요.")
        private String email;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
        private String password;

        @NotBlank(message = "닉네임을 입력해주세요.")
        @Pattern(regexp = "^[^\\s]+$", message = "닉네임에 공백을 포함할 수 없습니다.")
        @Size(min = 2, max = 6, message = "닉네임은 2자 이상 6자 이하이어야 합니다.")
        private String nickname;

        @Builder
        public UserSignup(String email, String password, String nickname) {
                this.email = email;
                this.password = password;
                this.nickname = nickname;
        }
}

