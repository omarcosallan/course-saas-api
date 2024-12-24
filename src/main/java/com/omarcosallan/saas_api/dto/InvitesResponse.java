package com.omarcosallan.saas_api.dto;

import com.omarcosallan.saas_api.domain.enums.Role;
import com.omarcosallan.saas_api.domain.invite.Invite;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record InvitesResponse(List<InviteMinDTO> invites) {
    public record InviteMinDTO(UUID id,
                            Role role,
                            String email,
                            LocalDateTime createdAt,
                            Author author) {
        public InviteMinDTO(Invite invite) {
            this(
                    invite.getId(),
                    invite.getRole(),
                    invite.getEmail(),
                    invite.getCreatedAt(),
                    new Author(invite.getAuthor())
            );
        }
    }
}
