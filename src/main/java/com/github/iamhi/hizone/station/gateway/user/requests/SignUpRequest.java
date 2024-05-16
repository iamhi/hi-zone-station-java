package com.github.iamhi.hizone.station.gateway.user.requests;

public record SignUpRequest(
    String username,
    String password,
    String email
) {
}
