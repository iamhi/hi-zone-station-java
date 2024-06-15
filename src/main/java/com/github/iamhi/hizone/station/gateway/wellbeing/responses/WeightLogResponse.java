package com.github.iamhi.hizone.station.gateway.wellbeing.responses;

import java.time.Instant;

public record WeightLogResponse(
    String uuid,
    String description,
    String imageUrl,
    Instant createdAt,
    Instant updateAt
) {
}
