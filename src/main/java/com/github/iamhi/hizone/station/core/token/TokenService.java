package com.github.iamhi.hizone.station.core.token;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Validated
public interface TokenService {

    Optional<String> generateRefreshToken(@NotBlank String username, @NotBlank String password);

    Optional<String> regenerateRefreshToken(@NotBlank String refreshToken);

    Optional<String> generateAccessToken(@NotBlank String refreshToken);

    Optional<TokenHolder> getAccessTokenHolder(String tokenUuid);

    boolean isValid(@NotNull TokenHolder tokenHolder);

    void invalidateToken(@NotBlank String refreshToken, @NotBlank String accessToken);
}
