package com.omarcosallan.saas_api.dto;

import com.omarcosallan.saas_api.domain.project.Project;
import com.omarcosallan.saas_api.domain.user.User;

import java.util.UUID;

public record OwnerResponseDTO(UUID id,
                               String name,
                               String avatarUrl) {
    public OwnerResponseDTO(User user) {
        this(user.getId(),
                user.getName(),
                user.getAvatarUrl()
        );
    }
}
