package com.omarcosallan.saas_api.dto;

import com.omarcosallan.saas_api.domain.user.User;

import java.util.UUID;

public record Author(UUID id,
                     String name,
                     String avatarUrl) {
    public Author(User author) {
        this(author.getId(), author.getName(), author.getAvatarUrl());
    }
}
