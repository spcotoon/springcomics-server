package com.springcomics.api.admin;

import com.springcomics.api.admin.adminDto.LoginDto;
import com.springcomics.api.config.util.CustomUser;
import com.springcomics.api.exception.InvalidSignInInformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final String adminId;
    private final String adminPw;
    private final String adminName;

    public AdminService(
            @Value("${springcomics.admin-id}") String adminId,
            @Value("${springcomics.admin-pw}") String adminPw,
            @Value("${springcomics.admin-name}") String adminName
    ) {
        this.adminId = adminId;
        this.adminPw = adminPw;
        this.adminName = adminName;
    }

    public Authentication authenticateJwtAdmin(LoginDto loginDto) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getId(), loginDto.getPw());

            Authentication auth = authenticateAdmin(authToken);

            SecurityContextHolder.getContext().setAuthentication(auth);

            return SecurityContextHolder.getContext().getAuthentication();
        } catch (BadCredentialsException e) {
            throw new InvalidSignInInformation();
        }
    }

    private Authentication authenticateAdmin(UsernamePasswordAuthenticationToken authToken) {
        String id = authToken.getName();
        String pw = (String) authToken.getCredentials();

        if (!adminId.equals(id) || !adminPw.equals(pw)) {
            throw new BadCredentialsException("Invalid admin credentials");
        }

        List<GrantedAuthority> authorities = getAuthorities();
        CustomUser customUser = new CustomUser("admin", adminName, "",authorities);
        return new UsernamePasswordAuthenticationToken(customUser, pw, authorities);
    }

    private List<GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );
    }


}
