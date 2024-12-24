package com.omarcosallan.saas_api.dto;

import com.omarcosallan.saas_api.domain.enums.Role;

import java.util.UUID;

public record MemberResponseDTO(UUID id,
                                UUID userId,
                                Role role,
                                String name,
                                String email,
                                String avatarUrl) {
}
