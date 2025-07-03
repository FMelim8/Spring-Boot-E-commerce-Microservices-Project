package com.example.sharedlib.security;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@Component
@SuppressWarnings("unchecked")
public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();;

    private final JwtConverterProperties jwtConverterProperties;

    public JwtConverter(JwtConverterProperties jwtConverterProperties) {
        this.jwtConverterProperties = jwtConverterProperties;
    }

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream()).collect(Collectors.toSet());
        return new JwtAuthenticationToken(jwt, authorities, getPreferredClaimName(jwt));
    }

    private String getPreferredClaimName(Jwt jwt) {
        String claimName = jwt.getClaim("preferred_username");
        if (jwtConverterProperties.getPrincipalAttribute() != null) {
            claimName = jwtConverterProperties.getPrincipalAttribute();
        }
        return jwt.getClaim(claimName);
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        return getGrantedAuthorities(jwt, log, jwtConverterProperties);
    }

    public static Collection<? extends GrantedAuthority> getGrantedAuthorities(Jwt jwt, Logger log, JwtConverterProperties jwtConverterProperties) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        log.info("Extracted JWT resource_access claims");

        if (resourceAccess == null) {
            log.warn("No resource_access claim found in the token!");
            return Set.of();
        }

        Map<String, Object> resource = (Map<String, Object>) resourceAccess.get(jwtConverterProperties.getResourceId());
        log.info("Extracted resources");

        if (resource == null) {
            log.warn("No resource found for client ID: {}", jwtConverterProperties.getResourceId());
            return Set.of();
        }

        Collection<String> resourceRoles = (Collection<String>) resource.get("roles");
        log.info("Extracted resource roles");

        if (resourceRoles == null) {
            log.warn("No roles found in resource for client ID: {}", jwtConverterProperties.getResourceId());
            return Set.of();
        }

        return resourceRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toSet());
    }
}

