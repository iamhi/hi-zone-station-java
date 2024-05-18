package com.github.iamhi.hizone.station.core.token;

import java.util.Optional;

public interface TokenService {

    Optional<String> generateRefreshToken(String username, String password);

    Optional<String> regenerateRefreshToken(String refreshToken);

    Optional<String> generateAccessToken(String refreshToken);

    Optional<TokenHolder> getAccessTokenHolder(String tokenUuid);

    boolean isValid(TokenHolder tokenHolder);

    void invalidateToken(String refreshToken, String accessToken);
}
