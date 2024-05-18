package com.github.iamhi.hizone.station.gateway.token.responses;

import org.apache.commons.lang3.tuple.Pair;

public record NewRefreshTokenResponse(
    String refreshToken,
    String accessToken
) {
    public NewRefreshTokenResponse(Pair<String, String> tokens) {
        this(
            tokens.getLeft(),
            tokens.getRight()
        );
    }
}
