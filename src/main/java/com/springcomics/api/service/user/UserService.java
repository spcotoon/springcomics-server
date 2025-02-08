package com.springcomics.api.service.user;

import com.springcomics.api.crypto.PasswordEncryptor;
import com.springcomics.api.domain.member.Artist;
import com.springcomics.api.domain.member.User;
import com.springcomics.api.exception.AlreadyExistsEmailException;
import com.springcomics.api.exception.AlreadyExistsNameException;
import com.springcomics.api.exception.InvalidEmail;
import com.springcomics.api.exception.InvalidSignInInformation;
import com.springcomics.api.repository.ArtistRepository;
import com.springcomics.api.repository.UserRepository;
import com.springcomics.api.request.CheckNickname;
import com.springcomics.api.request.UserLogin;
import com.springcomics.api.request.UserSignup;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;
    private final PasswordEncryptor passwordEncryptor;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    @Value("${springcomics.admin-name}")
    private String adminName;

    public void signup(UserSignup userSignup, boolean isEmailVerified) {

        if (userRepository.findByEmail(userSignup.getEmail()).isPresent()) {
            throw new AlreadyExistsEmailException();
        }

        if (userRepository.findByNickname(userSignup.getNickname()).isPresent()) {
            throw new AlreadyExistsNameException();
        }

        if (!isEmailVerified) {
            throw new InvalidEmail();
        }


        User user = User.builder()
                .email(userSignup.getEmail())
                .password(passwordEncryptor.encode(userSignup.getPassword()))
                .nickname(userSignup.getNickname())
                .build();

        user.verifyEmail();

        userRepository.save(user);
    }


    public void checkNickname(CheckNickname checkNickname) {
        Optional<User> existingNickname = userRepository.findByNickname(checkNickname.getNickname());
        Optional<Artist> existingArtist = artistRepository.findByPenName(checkNickname.getNickname());

        if (existingNickname.isPresent() || existingArtist.isPresent() || checkNickname.getNickname().equals(adminName)) {
            throw new AlreadyExistsNameException();
        }
    }


    public Authentication authenticateJwtUser(UserLogin userLogin) {
        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword());

            Authentication auth = authenticationManagerBuilder.getObject().authenticate(authToken);

            SecurityContextHolder.getContext().setAuthentication(auth);
            return SecurityContextHolder.getContext().getAuthentication();
        } catch (BadCredentialsException e) {
            throw new InvalidSignInInformation();
        }
    }

}
