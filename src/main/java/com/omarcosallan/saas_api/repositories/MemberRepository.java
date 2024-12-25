package com.omarcosallan.saas_api.repositories;

import com.omarcosallan.saas_api.domain.enums.Role;
import com.omarcosallan.saas_api.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByUserIdAndOrganizationSlug(UUID userId, String slug);

    Optional<Member> findByUserIdAndOrganizationId(UUID userId, UUID organizationId);

    List<Member> findByOrganizationIdOrderByRoleAsc(UUID id);

    Optional<Member> findByIdAndOrganizationId(UUID memberId, UUID id);

    void deleteByIdAndOrganizationId(UUID memberId, UUID id);

    Long countByOrganizationIdAndRoleNot(UUID organizationId, Role role);
}
