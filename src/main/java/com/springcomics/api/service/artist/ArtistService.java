package com.springcomics.api.service.artist;

import com.springcomics.api.exception.InvalidSignInInformation;
import com.springcomics.api.request.ArtistLogin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtistService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public Authentication authenticateJwtUser(ArtistLogin artistLogin) {
        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(artistLogin.getEmail(), artistLogin.getPassword());

            Authentication auth = authenticationManagerBuilder.getObject().authenticate(authToken);

            SecurityContextHolder.getContext().setAuthentication(auth);
            return SecurityContextHolder.getContext().getAuthentication();
        } catch (BadCredentialsException e) {
            throw new InvalidSignInInformation();
        }
    }


}
