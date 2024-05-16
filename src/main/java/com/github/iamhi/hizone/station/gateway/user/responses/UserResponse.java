package com.github.iamhi.hizone.station.gateway.user.responses;

import java.util.List;

public record UserResponse(
    String uuid,
    String username,
    String email,
    List<String> roles
) {

}
