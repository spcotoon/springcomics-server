package com.springcomics.api.request;

import lombok.Data;

@Data
public class UserLogin {

    private String email;
    private String password;

}
