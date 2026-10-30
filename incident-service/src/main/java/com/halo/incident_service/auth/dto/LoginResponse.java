package com.halo.incident_service.auth.dto;

import com.halo.incident_service.user.dto.UserResponse;

public record LoginResponse(String accessToken, String tokenType, long expiresInSeconds,
                            UserResponse user) {}