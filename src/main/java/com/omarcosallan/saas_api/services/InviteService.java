package com.omarcosallan.saas_api.services;

import com.omarcosallan.saas_api.domain.enums.Role;
import com.omarcosallan.saas_api.domain.invite.Invite;
import com.omarcosallan.saas_api.domain.member.Member;
import com.omarcosallan.saas_api.domain.organization.Organization;
import com.omarcosallan.saas_api.domain.user.User;
import com.omarcosallan.saas_api.dto.*;
import com.omarcosallan.saas_api.exceptions.BadRequestException;
import com.omarcosallan.saas_api.exceptions.InviteNotFoundException;
import com.omarcosallan.saas_api.exceptions.UnauthorizedException;
import com.omarcosallan.saas_api.repositories.InviteRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InviteService {

    @Autowired
    private InviteRepository inviteRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private UserService userService;

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

    public InviteResponseDTO getInvite(UUID inviteId) {
        Invite invite = inviteRepository.findById(inviteId)
                .orElseThrow(InviteNotFoundException::new);
        return new InviteResponseDTO(new InviteResponseDTO.InviteDTO(invite));
    }

    public InvitesMinResponseDTO getInvites(String slug) {
        Member member = memberService.getMember(slug);

        boolean canGetInvite = member.getRole() == Role.ADMIN;
        if (!canGetInvite) {
            throw new UnauthorizedException("You're not allowed to get organization invites.");
        }

        List<Invite> invites = inviteRepository.findByOrganizationIdOrderByCreatedAtDesc(member.getOrganization().getId());

        return new InvitesMinResponseDTO(
                invites.stream()
                        .map(InvitesMinResponseDTO.InviteMinDTO::new)
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public void acceptInvite(UUID inviteId) {
        User user = userService.authenticated();

        Invite invite = inviteRepository.findById(inviteId)
                .orElseThrow(() -> new BadRequestException("Invite not found or expired."));

        if (!invite.getEmail().equals(user.getEmail())) {
            throw new BadRequestException("This invite belongs to another user.");
        }

        memberService.create(user, invite.getOrganization(), invite.getRole());

        inviteRepository.deleteById(invite.getId());
    }

    @Transactional
    public void rejectInvite(UUID inviteId) {
        User user = userService.authenticated();

        Invite invite = inviteRepository.findById(inviteId)
                .orElseThrow(() -> new BadRequestException("Invite not found or expired."));

        if (!invite.getEmail().equals(user.getEmail())) {
            throw new BadRequestException("This invite belongs to another user.");
        }

        inviteRepository.deleteById(invite.getId());
    }

    @Transactional
    public void revokeInvite(String slug, UUID inviteId) {
        Member member = memberService.getMember(slug);

        boolean canDeleteInvite = member.getRole() == Role.ADMIN;
        if (!canDeleteInvite) {
            throw new UnauthorizedException("You're not allowed to delete an invite.");
        }

        Invite invite = inviteRepository.findByIdAndOrganizationId(inviteId, member.getOrganization().getId())
                .orElseThrow(InviteNotFoundException::new);

        inviteRepository.deleteById(inviteId);
    }

    public InvitesResponseDTO getPendingInvites() {
        User user = userService.authenticated();

        List<Invite> invites = inviteRepository.findByEmail(user.getEmail());

        return new InvitesResponseDTO(
                invites.stream()
                        .map(InvitesResponseDTO.InviteDTO::new)
                        .collect(Collectors.toList()));
    }
}
