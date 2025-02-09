package com.springcomics.api.config;

import com.springcomics.api.config.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfig corsConfig;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain artistSecurityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
                .sessionManagement((session)->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class)

                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/health").permitAll()

                                .requestMatchers(HttpMethod.POST, "/artist/signup").permitAll()
                                .requestMatchers(HttpMethod.POST, "/artist/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "/artist/logout").permitAll()
                                .requestMatchers(HttpMethod.POST, "/artist/**").hasRole("ARTIST")
                                .requestMatchers(HttpMethod.GET, "/artist/**").hasRole("ARTIST")

                                .requestMatchers(HttpMethod.GET, "/springcomics/get-one/**").hasRole("USER")
                                .requestMatchers(HttpMethod.GET, "/springcomics/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/springcomics/**").authenticated()
                                .requestMatchers(HttpMethod.POST, "/comment/**").hasRole("USER")

                                .requestMatchers(HttpMethod.GET, "/user/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/user/**").permitAll()

                                .requestMatchers(HttpMethod.POST, "/admin/manage/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/admin/manage/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/admin/manage/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/admin/manage/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/admin/auth/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/admin/auth/**").permitAll()

                                .anyRequest().authenticated()
                );

        http
                .exceptionHandling(httpSecurityExceptionHandling ->
                        httpSecurityExceptionHandling.accessDeniedHandler(customAccessDeniedHandler));

        return http.build();
    }
}