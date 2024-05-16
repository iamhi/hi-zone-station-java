package com.github.iamhi.hizone.station.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "hizone.station.authentication")
@Data
public class UserRoleConfig {

    private Map<String, String> roles;
}
