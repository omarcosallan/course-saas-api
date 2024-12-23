package com.omarcosallan.saas_api.repositories;

import com.omarcosallan.saas_api.domain.organization.Organization;
import com.omarcosallan.saas_api.dto.OrganizationMinDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    Optional<Organization> findByDomain(String domain);

    @Query("SELECT new com.omarcosallan.saas_api.dto.OrganizationMinDTO(o.id, o.name, o.slug, o.avatarUrl, m.role) FROM Organization o JOIN o.members m WHERE m.user.id = :userId")
    List<OrganizationMinDTO> findOrganizationsWithUserRoles(@Param("userId") UUID userId);
}
