package com.github.iamhi.hizone.station.config.security;

import com.github.iamhi.hizone.station.config.ExtendedUserDetails;
import com.github.iamhi.hizone.station.core.token.TokenHolder;
import com.github.iamhi.hizone.station.core.token.TokenService;
import com.github.iamhi.hizone.station.core.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthTokenAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_BEARER_PREFIX = "Bearer ";

    private final TokenService tokenService;

    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String accessToken = getAccessToken(request);

        Optional<TokenHolder> optionalAccessTokenHolder = tokenService.getAccessTokenHolder(accessToken);

        optionalAccessTokenHolder.flatMap(tokenHolder -> userService.findUserByUuid(tokenHolder.getUserUuid()))
            .ifPresent(userDto -> {
                ExtendedUserDetails extendedUserDetails = new ExtendedUserDetails(
                    userDto.uuid(),
                    userDto.username(),
                    StringUtils.EMPTY,
                    AuthorityUtils.createAuthorityList(userDto.roles())
                );

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    extendedUserDetails,
                    StringUtils.EMPTY,
                    extendedUserDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            });

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.isNotBlank(authorizationHeader) && authorizationHeader.startsWith(AUTHORIZATION_BEARER_PREFIX)) {
            authorizationHeader = authorizationHeader.replaceFirst(AUTHORIZATION_BEARER_PREFIX, "");
        }

        return authorizationHeader;
    }
}
