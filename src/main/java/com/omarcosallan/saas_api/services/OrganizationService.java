package com.omarcosallan.saas_api.services;

import com.omarcosallan.saas_api.domain.enums.Role;
import com.omarcosallan.saas_api.domain.member.Member;
import com.omarcosallan.saas_api.domain.organization.Organization;
import com.omarcosallan.saas_api.domain.user.User;
import com.omarcosallan.saas_api.dto.CreateOrganizationRequestDTO;
import com.omarcosallan.saas_api.dto.CreateOrganizationResponseDTO;
import com.omarcosallan.saas_api.exceptions.OrganizationDomainAlreadyExistsException;
import com.omarcosallan.saas_api.repositories.OrganizationRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.Optional;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public CreateOrganizationResponseDTO createOrganization(@Valid CreateOrganizationRequestDTO body) {
        User currentUser = userService.authenticated();

        if (body.domain() != null) {
            Optional<Organization> organizationByDomain = organizationRepository.findByDomain(body.domain());

            if (organizationByDomain.isPresent()) {
                throw new OrganizationDomainAlreadyExistsException();
            }
        }

        Organization organization = new Organization();
        organization.setName(body.name());
        organization.setSlug(createSlug(body.name()));
        organization.setDomain(body.domain());
        organization.setShouldAttachUsersByDomain(body.shouldAttachUsersByDomain());
        organization.setOwner(currentUser);
        Member member = new Member();
        member.setUser(currentUser);
        organization.addMember(member);
        member.setRole(Role.ADMIN);

        organizationRepository.save(organization);

        return new CreateOrganizationResponseDTO(organization.getId());
    }

    private String createSlug(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("\\p{M}", "").replaceAll("[^\\w\\s]", "").trim().replaceAll("\\s+", "-").toLowerCase();
    }
}
