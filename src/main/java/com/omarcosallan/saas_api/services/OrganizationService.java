package com.omarcosallan.saas_api.services;

import com.omarcosallan.saas_api.domain.enums.Role;
import com.omarcosallan.saas_api.domain.member.Member;
import com.omarcosallan.saas_api.domain.organization.Organization;
import com.omarcosallan.saas_api.domain.user.User;
import com.omarcosallan.saas_api.dto.CreateOrganizationRequestDTO;
import com.omarcosallan.saas_api.dto.CreateOrganizationResponseDTO;
import com.omarcosallan.saas_api.dto.OrganizationDTO;
import com.omarcosallan.saas_api.dto.OrganizationMinDTO;
import com.omarcosallan.saas_api.exceptions.OrganizationDomainAlreadyExistsException;
import com.omarcosallan.saas_api.exceptions.UnauthorizedException;
import com.omarcosallan.saas_api.repositories.MemberRepository;
import com.omarcosallan.saas_api.repositories.OrganizationRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MemberRepository memberRepository;

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

    public OrganizationDTO getOrganization(String slug) {
        Member member = memberRepository.findByUserIdAndOrganizationSlug(userService.authenticated().getId(), slug)
                .orElseThrow(UnauthorizedException::new);
        return new OrganizationDTO(member.getOrganization());
    }

    public List<OrganizationMinDTO> getOrganizations() {
        List<OrganizationMinDTO> orgs = organizationRepository.findOrganizationsWithUserRoles(userService.authenticated().getId());
        return orgs;
    }

    private String createSlug(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("\\p{M}", "").replaceAll("[^\\w\\s]", "").trim().replaceAll("\\s+", "-").toLowerCase();
    }
}
