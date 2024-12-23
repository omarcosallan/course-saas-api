package com.omarcosallan.saas_api.dto;

import java.util.UUID;

public record UserMinDTO(UUID id, String name, String email, String avatarUrl) {
}
