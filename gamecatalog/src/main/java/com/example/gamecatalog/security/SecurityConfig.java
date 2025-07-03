package com.example.gamecatalog.security;


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
                        auth.requestMatchers(HttpMethod.GET, "/api/games/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/games/reduceQuantity/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/games/update/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/games/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/games/create").hasRole("ADMIN")
                                .requestMatchers("/").permitAll()
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
