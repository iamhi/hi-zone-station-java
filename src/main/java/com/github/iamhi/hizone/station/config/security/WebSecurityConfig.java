package com.github.iamhi.hizone.station.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private static final String[] NOT_AUTHORIZE_PATHS = {
        "/actuator/**",
        "/error",
        "/authentication/user",
        "/auth/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/webjars/**",
        "/v3/**",
        "/user/signup",
        "/token/**"
    };

    private final UserDetailsService userDetailsService;

    private final AuthTokenAuthenticationFilter authTokenAuthenticationFilter;

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
            http.getSharedObject(AuthenticationManagerBuilder.class);

        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        http
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
            .cors(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(customizer ->
                customizer.requestMatchers(NOT_AUTHORIZE_PATHS)
                    .permitAll()
                    .anyRequest()
                    .fullyAuthenticated()
            )
            .userDetailsService(userDetailsService)
            .httpBasic(Customizer.withDefaults())
            .addFilterBefore(authTokenAuthenticationFilter, BasicAuthenticationFilter.class)
            .formLogin(Customizer.withDefaults());

        return http.build();
    }
}
