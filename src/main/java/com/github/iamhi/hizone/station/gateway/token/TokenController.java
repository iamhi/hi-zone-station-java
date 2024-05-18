package com.github.iamhi.hizone.station.gateway.token;

import com.github.iamhi.hizone.station.core.token.TokenService;
import com.github.iamhi.hizone.station.gateway.token.requests.AccessTokenRequest;
import com.github.iamhi.hizone.station.gateway.token.requests.InvalidateTokenRequest;
import com.github.iamhi.hizone.station.gateway.token.requests.NewRefreshTokenRequest;
import com.github.iamhi.hizone.station.gateway.token.requests.RenewRefreshTokenRequest;
import com.github.iamhi.hizone.station.gateway.token.responses.NewRefreshTokenResponse;
import com.github.iamhi.hizone.station.gateway.token.responses.TokenResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequestMapping("/token")
@RestController
public record TokenController(
    TokenService tokenService
) {

    @PostMapping("/access")
    Optional<TokenResponse> createAccessToken(@RequestBody AccessTokenRequest request) {
        return tokenService().generateAccessToken(request.refreshToken()).map(TokenResponse::new);
    }

    @PostMapping("/renew")
    Optional<TokenResponse> renewRefreshToken(@RequestBody RenewRefreshTokenRequest request) {
        return tokenService().regenerateRefreshToken(request.refreshToken()).map(TokenResponse::new);
    }

    @PostMapping("/generate")
    Optional<NewRefreshTokenResponse> newRefreshToken(@RequestBody NewRefreshTokenRequest request) {
        return tokenService().generateRefreshToken(request.username(), request.password()).map(refreshToken -> {
            String accessToken = tokenService.generateAccessToken(refreshToken).orElse(StringUtils.EMPTY);

            return Pair.of(refreshToken, accessToken);
        }).map(NewRefreshTokenResponse::new);
    }

    @PostMapping("/invalidate")
    @ResponseStatus(HttpStatus.OK)
    void invalidateToken(@RequestBody InvalidateTokenRequest request) {
        tokenService().invalidateToken(request.refreshToken(), request.accessToken());
    }
}
