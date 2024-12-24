package com.omarcosallan.saas_api.dto;

import com.omarcosallan.saas_api.domain.enums.Role;
import com.omarcosallan.saas_api.domain.invite.Invite;

import java.time.LocalDateTime;
import java.util.UUID;

public record InviteResponse(InviteDTO invite) {
    public record InviteDTO(UUID id,
                            Role role,
                            String email,
                            LocalDateTime createdAt,
                            OrganizationNameDTO organization,
                            Author author) {
        public InviteDTO(Invite invite) {
            this(
                    invite.getId(),
                    invite.getRole(),
                    invite.getEmail(),
                    invite.getCreatedAt(),
                    new OrganizationNameDTO(invite.getOrganization().getName()),
                    new Author(invite.getAuthor())
            );
        }
    }
}
