package com.github.iamhi.hizone.station.gateway.token.requests;

public record InvalidateTokenRequest(
    String accessToken,
    String refreshToken
) {
}
