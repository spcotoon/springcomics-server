package com.springcomics.api.controller.artist;

import com.springcomics.api.config.util.JwtUtil;
import com.springcomics.api.request.ArtistLogin;
import com.springcomics.api.response.LoginResponse;
import com.springcomics.api.service.artist.ArtistService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    @PostMapping("/artist/login")
    public ResponseEntity<String> login(@RequestBody ArtistLogin artistLogin, HttpServletResponse response) {
        Authentication authentication = artistService.authenticateJwtUser(artistLogin);

        String accessToken = JwtUtil.createAccessToken(authentication);
        String refreshToken = JwtUtil.createRefreshToken(artistLogin.getEmail());

        response.addCookie(JwtUtil.createCookie("accessToken", accessToken));
        response.addCookie(JwtUtil.createCookie("refreshToken", refreshToken));

        return ResponseEntity.status(HttpStatus.OK).body("success");
    }

    @GetMapping("/artist/auth")
    public ResponseEntity<LoginResponse> checkRole(HttpServletRequest request) {
        String token = JwtUtil.getJwtFromCookies(request, "accessToken");

        if (token != null) {
            Claims claims = JwtUtil.extractToken(token);

            String email = claims.get("email", String.class);
            String displayName = claims.get("displayName", String.class);
            String authority = claims.get("authority", String.class);

            LoginResponse response = LoginResponse.builder()
                    .email(email)
                    .displayName(displayName)
                    .authority(authority)
                    .build();

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


    @PostMapping("/artist/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {

        JwtUtil.deleteCookie(response, "accessToken");
        JwtUtil.deleteCookie(response, "refreshToken");

        SecurityContextHolder.clearContext();

        return ResponseEntity.ok("로그아웃 완료");
    }

}
