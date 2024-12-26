package com.omarcosallan.saas_api.dto;

import com.omarcosallan.saas_api.domain.enums.Role;

import java.util.UUID;

public record MembershipDTO(UUID id,
                            Role role,
                            UUID userId,
                            UUID organizationId) {
}
