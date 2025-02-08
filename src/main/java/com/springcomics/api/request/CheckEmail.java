package com.springcomics.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CheckEmail {

    @Email(message = "이메일을 정확히 입력하세요")
    @NotBlank
    private String email;
}
