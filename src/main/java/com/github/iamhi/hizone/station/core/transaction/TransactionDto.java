package com.github.iamhi.hizone.station.core.transaction;

import java.time.Instant;

public record TransactionDto(
    String uuid,
    String description,
    String category,
    Long value,
    Instant createdAt
) {
}
