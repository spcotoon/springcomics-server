package com.springcomics.api.config.util;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class CustomUser extends User {
    private final String displayName;

    public CustomUser(String username, String displayName, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.displayName = displayName;
    }
}
