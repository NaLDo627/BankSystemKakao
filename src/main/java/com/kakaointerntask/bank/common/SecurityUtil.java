package com.kakaointerntask.bank.common;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class SecurityUtil {
    public static boolean hasRole(Collection<? extends GrantedAuthority> authorities, String role) {
        for (GrantedAuthority auth : authorities) {
            if (auth.getAuthority().equals(role))
                return true;
        }
        return false;
    }
}
