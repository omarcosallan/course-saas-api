package com.omarcosallan.saas_api.repositories;

import com.omarcosallan.saas_api.domain.invite.Invite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InviteRepository extends JpaRepository<Invite, UUID> {
    Optional<Invite> findByEmailAndOrganizationId(String email, UUID id);
}
