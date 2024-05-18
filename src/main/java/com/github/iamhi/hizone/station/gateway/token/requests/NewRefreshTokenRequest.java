package com.github.iamhi.hizone.station.gateway.token.requests;

public record NewRefreshTokenRequest(
    String username,
    String password
) {
}
