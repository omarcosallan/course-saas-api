package com.omarcosallan.saas_api.services;

import com.omarcosallan.saas_api.domain.enums.Role;
import com.omarcosallan.saas_api.domain.invite.Invite;
import com.omarcosallan.saas_api.domain.member.Member;
import com.omarcosallan.saas_api.domain.organization.Organization;
import com.omarcosallan.saas_api.dto.CreateInviteRequestDTO;
import com.omarcosallan.saas_api.dto.CreateInviteResponseDTO;
import com.omarcosallan.saas_api.dto.InviteResponse;
import com.omarcosallan.saas_api.exceptions.BadRequestException;
import com.omarcosallan.saas_api.exceptions.InviteNotFoundException;
import com.omarcosallan.saas_api.exceptions.UnauthorizedException;
import com.omarcosallan.saas_api.repositories.InviteRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class InviteService {

    @Autowired
    private InviteRepository inviteRepository;

    @Autowired
    private MemberService memberService;

    @Transactional
    public CreateInviteResponseDTO createInvite(String slug, @Valid CreateInviteRequestDTO body) {
        Member member = memberService.getMember(slug);

        boolean canCreateInvite = member.getRole() == Role.ADMIN;
        if (!canCreateInvite) {
            throw new UnauthorizedException("You're not allowed to create new invites.");
        }

        String email = body.email();
        String[] emailParts = email.split("@");
        if (emailParts.length != 2) {
            throw new BadRequestException("Invalid email format.");
        }
        String domain = emailParts[1];

        Organization org = member.getOrganization();

        if (org.isShouldAttachUsersByDomain() &&
            domain.equals(org.getDomain())) {
            throw new BadRequestException("Users with " + domain + " domain will join your organization automatically on login.");
        }

        Optional<Invite> inviteWithSameEmail = inviteRepository.findByEmailAndOrganizationId(email, org.getId());

        if (inviteWithSameEmail.isPresent()) {
            throw new BadRequestException("A member with this e-mail already belongs to your organization.");
        }

        Invite invite = new Invite();
        invite.setOrganization(org);
        invite.setEmail(email);
        invite.setRole(body.role());
        invite.setAuthor(member.getUser());

        inviteRepository.save(invite);

        return new CreateInviteResponseDTO(invite.getId());
    }

    public InviteResponse getInvite(UUID inviteId) {
        Invite invite = inviteRepository.findById(inviteId)
                .orElseThrow(InviteNotFoundException::new);
        return new InviteResponse(new InviteResponse.InviteDTO(invite));
    }
}
