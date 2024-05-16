package com.github.iamhi.hizone.station.core.user;

import java.time.Instant;
import java.util.List;

public record UserDto(
    String uuid,
    String username,
    String email,
    List<String> roles,
    Instant createdAt,
    Instant updatedAt
) {
}
