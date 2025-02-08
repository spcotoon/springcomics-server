package com.springcomics.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ArtistSignup {
    private String email;
    private String password;
    private String penName;
    private String selfIntroduction;

}
