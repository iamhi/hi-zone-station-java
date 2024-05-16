package com.github.iamhi.hizone.station.gateway.user.requests;

public record AddRoleRequest(
    String role,
    String rolePassword
) {
}
