package com.omarcosallan.saas_api.dto;

import com.omarcosallan.saas_api.domain.enums.Role;

import java.util.UUID;

public record OrganizationMinDTO(UUID id,
                                 String name,
                                 String slug,
                                 String avatarUrl,
                                 Role role) {
}
