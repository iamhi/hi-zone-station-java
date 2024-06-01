package com.github.iamhi.hizone.station.core.token;

import com.github.iamhi.hizone.station.core.user.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class TokenServiceImpl implements TokenService {

    private final UserService userService;

    private final TokenStore tokenStore;

    @Override
    public Optional<String> generateRefreshToken(String username, String password) {
        String userUuid = userService.getUserUuid(username, password);

        if (StringUtils.isNotBlank(userUuid)) {
            TokenHolder tokenHolder = generateTokenHolder();

            tokenHolder.setType(TokenHolder.TYPE_REFRESH_TOKEN);
            tokenHolder.setValidTo(Instant.now().plusSeconds(TokenHolder.REFRESH_TOKEN_EXPIRATION));
            tokenHolder.setUserUuid(userUuid);
            tokenHolder.setParentUuid(userUuid);

            tokenStore.addToken(tokenHolder);

            return Optional.of(tokenHolder.getUuid());
        }

        return Optional.empty();
    }

    @Override
    public Optional<String> regenerateRefreshToken(String refreshToken) {
        Optional<TokenHolder> optionalOldTokenHolder = tokenStore.getTokenHolder(refreshToken);

        if (optionalOldTokenHolder.isPresent()) {
            TokenHolder oldTokenHolder = optionalOldTokenHolder.get();

            if (isValid(oldTokenHolder) && oldTokenHolder.getType().equals(TokenHolder.TYPE_REFRESH_TOKEN)) {
                TokenHolder tokenHolder = generateTokenHolder();

                tokenHolder.setType(TokenHolder.TYPE_REFRESH_TOKEN);
                tokenHolder.setValidTo(Instant.now().plusSeconds(TokenHolder.REFRESH_TOKEN_EXPIRATION));
                tokenHolder.setUserUuid(oldTokenHolder.getUserUuid());
                tokenHolder.setParentUuid(oldTokenHolder.getUserUuid());

                tokenStore.addToken(tokenHolder);
                tokenStore.removeToken(oldTokenHolder);

                return Optional.of(tokenHolder.getUuid());
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<String> generateAccessToken(String refreshToken) {
        Optional<TokenHolder> optionalRefreshTokenHolder = tokenStore.getTokenHolder(refreshToken);

        if (optionalRefreshTokenHolder.isPresent()) {
            TokenHolder refreshTokenHolder = optionalRefreshTokenHolder.get();

            if (isValid(refreshTokenHolder) && refreshTokenHolder.getType().equals(TokenHolder.TYPE_REFRESH_TOKEN)) {
                TokenHolder tokenHolder = generateTokenHolder();

                tokenHolder.setType(TokenHolder.TYPE_ACCESS_TOKEN);
                tokenHolder.setValidTo(Instant.now().plusSeconds(TokenHolder.ACCESS_TOKEN_EXPIRATION));
                tokenHolder.setUserUuid(refreshTokenHolder.getUserUuid());
                tokenHolder.setParentUuid(refreshToken);

                tokenStore.addToken(tokenHolder);

                return Optional.of(tokenHolder.getUuid());
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<TokenHolder> getAccessTokenHolder(String tokenUuid) {
        return tokenStore.getTokenHolder(tokenUuid)
            .filter(tokenHolder -> tokenHolder.getType().equals(TokenHolder.TYPE_ACCESS_TOKEN))
            .filter(tokenStore::isValid);
    }

    @Override
    public boolean isValid(TokenHolder tokenHolder) {
        return tokenStore.isValid(tokenHolder);
    }

    @Override
    public void invalidateToken(String refreshToken, String accessToken) {
        Optional.ofNullable(accessToken).flatMap(tokenStore::getTokenHolder)
            .ifPresent(tokenStore::removeToken);

        Optional.ofNullable(refreshToken).flatMap(tokenStore::getTokenHolder)
            .ifPresent(refreshTokenHolder -> {
                tokenStore.removeToken(refreshTokenHolder);

                tokenStore.getTokensWithParent(refreshTokenHolder.getUuid())
                    .forEach(tokenStore::removeToken);
            });
    }

    private TokenHolder generateTokenHolder() {
        TokenHolder tokenHolder = new TokenHolder();

        tokenHolder.setUuid(UUID.randomUUID().toString());

        return tokenHolder;
    }
}
