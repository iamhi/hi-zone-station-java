package com.github.iamhi.hizone.station.gateway.user.requests;

public record LoginRequest(
    String username,
    String password
) {
}
