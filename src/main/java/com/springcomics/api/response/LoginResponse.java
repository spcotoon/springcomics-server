package com.springcomics.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String email;
    private String displayName;
    private String authority;

    @Builder
    public LoginResponse(String email, String displayName, String authority) {
        this.email = email;
        this.displayName = displayName;
        this.authority = authority;
    }
}
