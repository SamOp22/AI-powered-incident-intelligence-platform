package com.halo.incident_service.user.dto;

import com.halo.incident_service.user.entity.Role;
import com.halo.incident_service.user.entity.User;

import java.time.Instant;

public record UserResponse(Long id, String name, String email, Role role, Instant createdAt) {

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(),
                user.getRole(), user.getCreatedAt());
    }
}