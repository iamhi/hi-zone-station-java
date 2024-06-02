package com.github.iamhi.hizone.station.gateway.transaction.requests;

public record TransactionCreateRequest(
    String description,
    String category,
    Long value
) {
}
