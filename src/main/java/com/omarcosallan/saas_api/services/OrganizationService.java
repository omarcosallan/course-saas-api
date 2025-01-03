package com.omarcosallan.saas_api.services;

import com.omarcosallan.saas_api.domain.enums.Role;
import com.omarcosallan.saas_api.domain.member.Member;
import com.omarcosallan.saas_api.domain.organization.Organization;
import com.omarcosallan.saas_api.domain.user.User;
import com.omarcosallan.saas_api.dto.*;
import com.omarcosallan.saas_api.exceptions.OrganizationDomainAlreadyExistsException;
import com.omarcosallan.saas_api.exceptions.UnauthorizedException;
import com.omarcosallan.saas_api.repositories.OrganizationRepository;
import com.omarcosallan.saas_api.utils.SlugUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MemberService memberService;

    @Transactional
    public CreateOrganizationResponseDTO createOrganization(CreateOrganizationRequestDTO body) {
        User currentUser = userService.authenticated();

        if (body.domain() != null) {
            Optional<Organization> organizationByDomain = organizationRepository.findByDomain(body.domain());

            if (organizationByDomain.isPresent()) {
                throw new OrganizationDomainAlreadyExistsException();
            }
        }

        Organization organization = new Organization();
        organization.setName(body.name());
        organization.setSlug(SlugUtils.createSlug(body.name()));
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

    public OrganizationResponseDTO getOrganization(String slug) {
        Member member = memberService.getMember(slug);
        return new OrganizationResponseDTO(new OrganizationDTO(member.getOrganization()));
    }

    public OrganizationsResponseDTO getOrganizations() {
        UUID userId = userService.authenticated().getId();
        return new OrganizationsResponseDTO(organizationRepository.findOrganizationsWithUserRoles(userId));
    }

    @Transactional
    public void updateOrganization(String slug, UpdateOrganizationRequestDTO body) {
        Member member = memberService.getMember(slug);

        if (member.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("You're not allowed to update this organization.");
        }

        Organization organization = member.getOrganization();

        if (body.domain() != null) {
            Optional<Organization> organizationByDomain = organizationRepository.findFirstByDomainAndIdNot(body.domain(), organization.getId());

            if (organizationByDomain.isPresent()) {
                throw new OrganizationDomainAlreadyExistsException();
            }
        }

        organization.setName(body.name());
        organization.setDomain(body.domain());
        organization.setShouldAttachUsersByDomain(body.shouldAttachUsersByDomain());

        organizationRepository.save(organization);
    }

    @Transactional
    public void shutdownOrganization(String slug) {
        Member member = memberService.getMember(slug);

        if (member.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("You're not allowed to delete this organization.");
        }

        Organization organization = member.getOrganization();

        organizationRepository.deleteById(organization.getId());
    }

    @Transactional
    public void transferOrganization(String slug, TransferOrganizationRequestDTO body) {
        Member member = memberService.getMember(slug);
        Organization organization = member.getOrganization();

        boolean canTransferOwnership = member.getRole() == Role.ADMIN
                && organization.getOwner().equals(member.getUser());
        if (!canTransferOwnership) {
            throw new UnauthorizedException("You're not allowed to transfer this organization ownership.");
        }

        Optional<Member> transferMembership = memberService.findByUserIdAndOrganizationId(body.transferToUserId(), organization.getId());

        if (transferMembership.isEmpty()) {
            throw new UnauthorizedException("You're not a member of this organization.");
        }

        transferMembership.get().setRole(Role.ADMIN);
        organization.setOwner(transferMembership.get().getUser());

        organizationRepository.save(organization);
    }

    public MembershipResponseDTO getMembership(String slug) {
        Member member = memberService.getMember(slug);

        return new MembershipResponseDTO(new MembershipDTO(
                member.getId(),
                member.getRole(),
                member.getUser().getId(),
                member.getOrganization().getId())
        );
    }
}
