package com.github.iamhi.hizone.station.config.security;

import com.github.iamhi.hizone.station.config.ExtendedUserDetails;
import com.github.iamhi.hizone.station.core.user.MemberCache;
import com.github.iamhi.hizone.station.core.user.MemberCacheImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

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

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    MemberCache memberCache() {
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            ExtendedUserDetails principal = (ExtendedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            return new MemberCacheImpl(principal::getUuid, principal::getUsername);
        }

        return new MemberCacheImpl(
            () -> StringUtils.EMPTY,
            () -> StringUtils.EMPTY
        );
    }
}
