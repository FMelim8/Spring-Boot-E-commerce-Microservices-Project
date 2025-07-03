package com.example.keycloak_service.security;


import com.example.sharedlib.exception.CustomAccessDeniedHandler;
import com.example.sharedlib.exception.CustomAuthenticationEntryPoint;
import com.example.sharedlib.security.JwtConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtConverter jwtConverter;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((auth) ->
                    auth.requestMatchers(HttpMethod.GET, "/api/visitor").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/register").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/user/**").hasRole("USER")
                            .requestMatchers(HttpMethod.GET, "/api/admin/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET, "/api/userDetails").hasAnyRole("ADMIN", "USER")
                            .requestMatchers(HttpMethod.GET, "/api/userDetails/*").hasRole("ADMIN")
                            .anyRequest().permitAll())
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint)
                );

        http.sessionManagement(session -> session.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS
        ));

        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(
                jwt -> jwt.jwtAuthenticationConverter(jwtConverter)
            ).authenticationEntryPoint(authenticationEntryPoint)
        );

        return http.build();
    }

}
