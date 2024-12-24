package com.omarcosallan.saas_api.dto;

import com.omarcosallan.saas_api.domain.project.Project;

import java.util.UUID;

public record ProjectResponseDTO(UUID id,
                                 String description,
                                 String name,
                                 String slug,
                                 String avatarUrl,
                                 UUID organizationId,
                                 UUID ownerId,
                                 OwnerResponseDTO owner) {
    public ProjectResponseDTO(Project project) {
        this(project.getId(),
                project.getDescription(),
                project.getName(),
                project.getSlug(),
                project.getAvatarUrl(),
                project.getOrganization().getId(),
                project.getOwner().getId(),
                new OwnerResponseDTO(project.getOwner())
        );
    }
}
