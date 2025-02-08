package com.springcomics.api.domain.member;

import com.springcomics.api.domain.DateOnlyBaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends DateOnlyBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String email;
    private String password;

    private String nickname;

    private boolean emailVerified = false;

    //todo 관심웹툰, 별점?등 추가..


    @Builder
    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public void verifyEmail() {
        this.emailVerified = true;
    }
}
