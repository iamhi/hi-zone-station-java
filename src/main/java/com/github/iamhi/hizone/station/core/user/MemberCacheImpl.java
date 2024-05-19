package com.github.iamhi.hizone.station.core.user;

import java.util.function.Supplier;

public record MemberCacheImpl(
    Supplier<String> uuidProvider,
    Supplier<String> userProvider) implements MemberCache {

    @Override
    public String getUuid() {
       return uuidProvider.get();
    }

    @Override
    public String getUsername() {
        return userProvider.get();
    }
}
