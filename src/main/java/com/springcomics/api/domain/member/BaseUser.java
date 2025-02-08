package com.springcomics.api.domain.member;

import com.springcomics.api.domain.DateOnlyBaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@MappedSuperclass
public abstract class BaseUser extends DateOnlyBaseEntity {

    private String email;
    private String password;

    public void encodePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
