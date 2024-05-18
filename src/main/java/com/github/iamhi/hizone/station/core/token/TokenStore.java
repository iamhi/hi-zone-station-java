package com.github.iamhi.hizone.station.core.token;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
class TokenStore {

    private final List<TokenHolder> tokenHolderList = Collections.synchronizedList(new ArrayList<>());

    void addToken(TokenHolder tokenHolder) {
        tokenHolderList.add(tokenHolder);
    }

    void removeToken(TokenHolder tokenHolder) {
        tokenHolderList.remove(tokenHolder);
    }

    @Scheduled(fixedDelay = 1000 * 60 * 3)
    void removeExpiredTokens() {
        List<TokenHolder> expiredTokens = new ArrayList<>();
        Instant now = Instant.now();

        tokenHolderList.forEach(tokenHolder -> {
            if (now.isAfter(tokenHolder.getValidTo())) {
                expiredTokens.add(tokenHolder);
            }
        });

        tokenHolderList.removeAll(expiredTokens);
    }

    Optional<TokenHolder> getTokenHolder(String tokenUuid) {
        for (TokenHolder tokenHolder : tokenHolderList) {
            if (tokenHolder.getUuid().equals(tokenUuid)) {
                return Optional.of(tokenHolder);
            }
        }

        return Optional.empty();
    }

    boolean isValid(TokenHolder tokenHolder) {
        return Instant.now().isBefore(tokenHolder.getValidTo());
    }

    List<TokenHolder> getTokensWithParent(String parentUuid) {
        return tokenHolderList.stream().
            filter(tokenHolder -> tokenHolder.getParentUuid().equals(parentUuid))
            .toList();
    }
}
