package com.github.iamhi.hizone.station.core.wellbeing.dto;

import java.time.Instant;

public record WeightLogDto(
    String uuid,
    String ownerUuid,
    String description,
    Instant createdAt,
    Instant updatedAt
) {
}
