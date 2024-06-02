package com.github.iamhi.hizone.station.gateway.transaction.responses;

import java.time.Instant;

public record TransactionResponse(
    String uuid,
    String description,
    String category,
    Long value,
    Instant createdAt
) {
}
