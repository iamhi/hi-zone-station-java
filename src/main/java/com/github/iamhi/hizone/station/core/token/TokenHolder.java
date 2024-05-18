package com.github.iamhi.hizone.station.core.token;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class TokenHolder {

    public static final String TYPE_ACCESS_TOKEN = "ACCESS_TOKEN";

    public static final String TYPE_REFRESH_TOKEN = "REFRESH_TOKEN";

    public static final long ACCESS_TOKEN_EXPIRATION = 60 * 15L;

    public static final long REFRESH_TOKEN_EXPIRATION = 60 * 60 * 24 * 15L;

    private String uuid;

    private String userUuid;

    private String parentUuid;

    private Instant validTo;

    private String type;
}
