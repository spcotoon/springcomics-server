package com.springcomics.api.service;

import com.springcomics.api.crypto.PasswordEncryptor;
import com.springcomics.api.repository.UserRepository;
import com.springcomics.api.request.ArtistSignup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("회원가입성공")
    public void signup() throws Exception {
        PasswordEncryptor encoder = new PasswordEncryptor();
        //given
        ArtistSignup request = ArtistSignup.builder()
                .email("aa@a.com")
                .password("11")
                .build();

        //when
//        authService.signup(request);

        //then
        assertEquals(1, userRepository.count());

    }


}