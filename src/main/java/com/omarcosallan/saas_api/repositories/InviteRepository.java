package com.omarcosallan.saas_api.repositories;

import com.omarcosallan.saas_api.domain.invite.Invite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InviteRepository extends JpaRepository<Invite, UUID> {
    Optional<Invite> findByEmailAndOrganizationId(String email, UUID id);

    List<Invite> findByOrganizationIdOrderByCreatedAtDesc(UUID id);

    Optional<Invite> findByIdAndOrganizationId(UUID inviteId, UUID id);

    List<Invite> findByEmail(String email);
}
