package com.springcomics.api.config;

import com.springcomics.api.config.util.CustomUser;
import com.springcomics.api.domain.member.Artist;
import com.springcomics.api.domain.member.User;
import com.springcomics.api.exception.InvalidEmail;
import com.springcomics.api.exception.InvalidSignInInformation;
import com.springcomics.api.repository.ArtistRepository;
import com.springcomics.api.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MultipleUserDetailsService implements UserDetailsService {

    private final ArtistRepository artistRepository;
    private final UserRepository userRepository;
    private final HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (isArtist(username) && !isArtistLoginRequest()) {
            throw new InvalidEmail();
        }

        if (isUser(username) && !isReaderLoginRequest()) {
            throw new InvalidEmail();
        }


        if (isArtist(username)) {
            return loadArtist(username);
        } else {
            return loadUser(username);
        }
    }

    private boolean isArtist(String username) {
        return username.endsWith("@spring.comics.artist");
    }

    private boolean isUser(String username) {
        return !username.endsWith("@spring.comics.artist");
    }


    private boolean isArtistLoginRequest() {
        return request.getRequestURI().contains("/artist/login");
    }

    private boolean isReaderLoginRequest() {
        return request.getRequestURI().contains("/user/login");
    }

    private UserDetails loadArtist(String email) {
        Artist artist = artistRepository.findByEmail(email).orElseThrow(InvalidSignInInformation::new);
        return new CustomUser(artist.getEmail(), artist.getPenName(), artist.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_ARTIST")));
    }

    private UserDetails loadUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(InvalidSignInInformation::new);
        return new CustomUser(user.getEmail(), user.getNickname(), user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }

}
