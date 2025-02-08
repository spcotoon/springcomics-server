package com.springcomics.api.admin;

import com.springcomics.api.admin.adminDto.LoginDto;
import com.springcomics.api.config.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/admin/auth/login")
    public ResponseEntity<String> adminLogin(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        Authentication authentication = adminService.authenticateJwtAdmin(loginDto);

        String accessToken = JwtUtil.createAccessToken(authentication);
        String refreshToken = JwtUtil.createRefreshToken(null);

            response.addCookie(JwtUtil.createCookie("accessToken", accessToken));
            response.addCookie(JwtUtil.createCookie("refreshToken", refreshToken));


        return ResponseEntity.status(HttpStatus.OK).body("success");
    }

    @GetMapping("/admin/manage/auth")
    public ResponseEntity<String> checkAdmin(HttpServletRequest request) {
        String token = JwtUtil.getJwtFromCookies(request, "accessToken");

        if (token != null) {
            Claims claims = JwtUtil.extractToken(token);
            String adminName = claims.get("displayName", String.class);

            return ResponseEntity.status(HttpStatus.OK).body(adminName);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/admin/auth/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {

        JwtUtil.deleteCookie(response, "accessToken");
        JwtUtil.deleteCookie(response, "refreshToken");

        SecurityContextHolder.clearContext();

        return ResponseEntity.ok("로그아웃 완료");
    }




}
