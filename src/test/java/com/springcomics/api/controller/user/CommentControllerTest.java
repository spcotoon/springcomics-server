package com.springcomics.api.controller.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.springcomics.api.config.util.CustomUser;
import com.springcomics.api.config.util.JwtUtil;
import com.springcomics.api.domain.member.User;
import com.springcomics.api.exception.UserNotFound;
import com.springcomics.api.repository.UserRepository;
import com.springcomics.api.repository.comic.ComicBodyRepository;
import com.springcomics.api.request.CommentPost;
import com.springcomics.api.request.UserLogin;
import com.springcomics.api.request.UserSignup;
import com.springcomics.api.service.user.UserService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("로그인 후 댓글입력")
    public void 댓글포스트() throws Exception {
        // given (테스트 데이터 설정)
        Long comicBodyId = 1L;  // 테스트할 코믹 ID
        CommentPost commentPost = new CommentPost("이건 테스트 댓글입니다!");

        UserSignup userSignup = UserSignup.builder()
                .email("test@test.com")
                .nickname("유저")
                .password("123123123")
                .build();
        userService.signup(userSignup, false);

        User user = userRepository.findByEmail("test@test.com").orElseThrow(UserNotFound::new);

        UserLogin userLogin = new UserLogin();
        userLogin.setEmail(user.getEmail());
        userLogin.setPassword(user.getPassword());
        Authentication authentication = userService.authenticateJwtUser(userLogin);

        String json = objectMapper.writeValueAsString(commentPost);

        // JWT 토큰 생성 (Authentication 객체 사용)
        String token = JwtUtil.createAccessToken(authentication);
        Cookie tokenCookie = new Cookie("accessToken", token);

        // when & then (요청 보내고 검증)
        mockMvc.perform(post("/comment/{comicBodyId}", comicBodyId)  // 패스배리어블 방식
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .cookie(tokenCookie))
                .andExpect(status().isOk())  // 200 응답이 오는지 확인
                .andExpect(jsonPath("$.message").value("댓글 작성 성공")) // 응답 메시지 확인
                .andDo(print());  // 요청 및 응답 로그 출력
    }

}