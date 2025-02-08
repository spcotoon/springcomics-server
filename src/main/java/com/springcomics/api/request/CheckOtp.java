package com.springcomics.api.request;

import lombok.Data;

@Data
public class CheckOtp {

    private String email;
    private String otp;
}
