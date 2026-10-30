package com.halo.incident_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(
        String secret,
        @DefaultValue("incident-platform") String issuer,
        @DefaultValue("60m") Duration expiry) {

    public JwtProperties {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("app.jwt.secret must be set");
        }
        if (expiry.isNegative() || expiry.isZero()) {
            throw new IllegalStateException("app.jwt.expiry must be positive");
        }
    }
}