package com.springcomics.api.config.filter;

import com.springcomics.api.config.util.CustomUser;
import com.springcomics.api.config.util.JwtUtil;
import com.springcomics.api.config.util.ResponseUtil;
import com.springcomics.api.exception.InvalidJwtToken;
import com.springcomics.api.exception.UserNotLogin;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private static final List<String> EXCLUDE_URLS = List.of(
            "/artist/login",
            "/artist/logout",
            "/user/login",
            "/user/signup",
            "/user/logout",
            "/user/check-email",
            "/user/check-nickname",
            "/user/check-otp",
            "/admin/auth/login",
            "/admin/auth/logout",
            "/springcomics/comics",
            "/springcomics/pf-comics",
            "/health"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if (EXCLUDE_URLS.stream().anyMatch(requestURI::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String accessToken = JwtUtil.getJwtFromCookies(request, "accessToken");

            if (accessToken == null || accessToken.isEmpty()) {
                throw new UserNotLogin();
            }

            if (!JwtUtil.validateToken(accessToken)) {

                String refreshToken = JwtUtil.getJwtFromCookies(request, "refreshToken");

                if (refreshToken != null && JwtUtil.validateToken(refreshToken)) {

                    Claims claims = JwtUtil.extractToken(accessToken);
                    String email = claims.get("email", String.class);
                    String displayName = claims.get("displayName", String.class);
                    String authorities = claims.get("authority", String.class);

                    CustomUser customUser = new CustomUser(email, displayName, "", List.of(new SimpleGrantedAuthority(authorities)));

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(customUser, null, customUser.getAuthorities());

                    String newAccessToken = JwtUtil.reissueAccessToken(email, displayName, authorities);

                    response.addCookie(JwtUtil.createCookie("accessToken", newAccessToken));

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                } else {
                    throw new UserNotLogin();
                }

            } else {
                Claims claims = JwtUtil.extractToken(accessToken);
                String email = claims.get("email", String.class);
                String displayName = claims.get("displayName", String.class);
                String authorities = claims.get("authority", String.class);

                CustomUser customUser = new CustomUser(email, displayName, "", List.of(new SimpleGrantedAuthority(authorities)));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(customUser, null, customUser.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);

        } catch (UserNotLogin ex) {
            ResponseUtil.sendErrorResponse(response, ex.getStatusCode(), ex.getMessage());
        } catch (InvalidJwtToken e) {

            response.addCookie(JwtUtil.createExpiredCookie("accessToken"));
            response.addCookie(JwtUtil.createExpiredCookie("refreshToken"));


            ResponseUtil.sendErrorResponse(response, e.getStatusCode(), e.getMessage());
        }
    }
}
