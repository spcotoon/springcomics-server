package com.springcomics.api.controller.user;

import com.springcomics.api.config.util.JwtUtil;
import com.springcomics.api.domain.member.OtpToken;
import com.springcomics.api.exception.AlreadyExistsNameException;
import com.springcomics.api.exception.InvalidEmail;
import com.springcomics.api.repository.OtpTokenRepository;
import com.springcomics.api.request.*;
import com.springcomics.api.response.LoginResponse;
import com.springcomics.api.service.auth.OtpService;
import com.springcomics.api.service.user.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final OtpService otpService;
    private final UserService userService;
    private final OtpTokenRepository otpTokenRepository;


    @PostMapping("/user/check-email")
    public ResponseEntity<String> getOtp(@Validated @RequestBody CheckEmail checkEmail, BindingResult bindingResult) {


        otpService.generateOtp(checkEmail.getEmail());

        return ResponseEntity.ok("인증코드가 이메일로 전송되었습니다.");
    }

    @PostMapping("/user/check-otp")
    public ResponseEntity<String> verifyOtp(@Validated @RequestBody CheckOtp checkOtp) {
        boolean isValid = otpService.verifyOtp(checkOtp.getEmail(), checkOtp.getOtp());

        if (!isValid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 실패");
        }


        return ResponseEntity.ok("인증 완료");
    }

    @PostMapping("/user/check-nickname")
    public ResponseEntity<String> checkNickname(@Validated @RequestBody CheckNickname checkNickname) {

        try {
            userService.checkNickname(checkNickname);
            return ResponseEntity.status(200).body("사용 가능한 닉네임입니다.");
        } catch (AlreadyExistsNameException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    @PostMapping("/user/signup")
    public ResponseEntity<String> signup(@Validated @RequestBody UserSignup userSignup) {

        OtpToken otpToken = otpTokenRepository.findByEmail(userSignup.getEmail()).orElseThrow(InvalidEmail::new);

        userService.signup(userSignup, otpToken.isVerified());

        return ResponseEntity.ok("success");
    }

    @PostMapping("/user/login")
    public ResponseEntity<String> login(@RequestBody UserLogin userLogin, HttpServletResponse response) {

        if (userLogin.getEmail().endsWith("artist")) {
            throw new InvalidEmail();
        }

        Authentication authentication = userService.authenticateJwtUser(userLogin);

        String accessToken = JwtUtil.createAccessToken(authentication);
        String refreshToken = JwtUtil.createRefreshToken(authentication.getName());

        response.addCookie(JwtUtil.createCookie("accessToken", accessToken));
        response.addCookie(JwtUtil.createCookie("refreshToken", refreshToken));

        return ResponseEntity.status(HttpStatus.OK).body("success");
    }

    @GetMapping("/user-auth/auth")
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

    @PostMapping("/user/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {

        JwtUtil.deleteCookie(response, "accessToken");
        JwtUtil.deleteCookie(response, "refreshToken");

        return ResponseEntity.ok("로그아웃 완료");
    }

}
