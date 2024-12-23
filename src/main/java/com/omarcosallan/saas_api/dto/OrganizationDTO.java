package com.omarcosallan.saas_api.dto;

import com.omarcosallan.saas_api.domain.organization.Organization;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrganizationDTO(
        UUID id,
        String name,
        String slug,
        String domain,
        boolean shouldAttachUsersByDomain,
        String avatarUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UUID ownerId
) {
    public OrganizationDTO(Organization organization) {
        this(
                organization.getId(),
                organization.getName(),
                organization.getSlug(),
                organization.getDomain(),
                organization.isShouldAttachUsersByDomain(),
                organization.getAvatarUrl(),
                organization.getCreatedAt(),
                organization.getUpdatedAt(),
                organization.getOwner().getId()
        );
    }
}
