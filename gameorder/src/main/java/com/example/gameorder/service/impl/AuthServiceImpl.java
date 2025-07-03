package com.example.gameorder.service.impl;

import com.example.sharedlib.security.JwtConverterProperties;
import com.example.gameorder.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static com.example.sharedlib.security.JwtConverter.getGrantedAuthorities;

@Service
@Log4j2
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtConverterProperties jwtConverterProperties;

    @Override
    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getClaim("sub");
        }
        throw new IllegalStateException("User is not authenticated");
    }

    @Override
    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return extractResourceRoles(jwt).toString().contains("ROLE_ADMIN");
        }
        return false;
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        return getGrantedAuthorities(jwt, log, jwtConverterProperties);
    }
}
