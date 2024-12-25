package com.omarcosallan.saas_api.services;

import com.omarcosallan.saas_api.domain.enums.Role;
import com.omarcosallan.saas_api.domain.member.Member;
import com.omarcosallan.saas_api.domain.organization.Organization;
import com.omarcosallan.saas_api.domain.user.User;
import com.omarcosallan.saas_api.dto.MemberResponseDTO;
import com.omarcosallan.saas_api.dto.MembersResponseDTO;
import com.omarcosallan.saas_api.dto.UpdateMemberRequestDTO;
import com.omarcosallan.saas_api.exceptions.MemberNotFoundException;
import com.omarcosallan.saas_api.exceptions.UnauthorizedException;
import com.omarcosallan.saas_api.repositories.MemberRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UserService userService;

    public Member getMember(String slug) {
        return memberRepository.findByUserIdAndOrganizationSlug(userService.authenticated().getId(), slug)
                .orElseThrow(() -> new UnauthorizedException("You're not a member of this organization."));
    }

    public MembersResponseDTO getMembers(String slug) {
        Member member = getMember(slug);

        boolean canGetUser = member.getRole() == Role.MEMBER || member.getRole() == Role.ADMIN;
        if (!canGetUser) {
            throw new UnauthorizedException("You're not allowed to see organization members.");
        }

        List<Member> members = memberRepository.findByOrganizationIdOrderByRoleAsc(member.getOrganization().getId());

        return new MembersResponseDTO(
                members.stream().map(m -> new MemberResponseDTO(
                m.getId(),
                m.getUser().getId(),
                m.getRole(),
                m.getUser().getName(),
                m.getUser().getEmail(),
                m.getUser().getAvatarUrl()
        )).collect(Collectors.toList()));
    }

    public Optional<Member> findByUserIdAndOrganizationId(UUID transferToUserId, UUID organizationId) {
        return memberRepository.findByUserIdAndOrganizationId(transferToUserId, organizationId);
    }

    @Transactional
    public void updateMember(String slug, UUID memberId, @Valid UpdateMemberRequestDTO body) {
        Member member = getMember(slug);

        boolean canUpdateUser = member.getRole() == Role.ADMIN;
        if (!canUpdateUser) {
            throw new UnauthorizedException("You're not allowed to update this member.");
        }

        Member updatingMember = memberRepository.findByIdAndOrganizationId(memberId, member.getOrganization().getId())
                .orElseThrow(MemberNotFoundException::new);

        updatingMember.setRole(body.role());

        memberRepository.save(updatingMember);
    }

    @Transactional
    public void removeMember(String slug, UUID memberId) {
        Member member = getMember(slug);

        boolean canDeleteUser = member.getRole() == Role.ADMIN;
        if (!canDeleteUser) {
            throw new UnauthorizedException("You're not allowed to remove this member from organization.");
        }

        memberRepository.deleteByIdAndOrganizationId(memberId, member.getOrganization().getId());
    }

    @Transactional
    public Member create(User user, Organization org, Role role) {
        Member member = new Member();
        member.setUser(user);
        member.setOrganization(org);
        member.setRole(role);
        return memberRepository.save(member);
    }

    public Long countByOrganizationIdAndRoleNot(UUID organizationId, Role role) {
        return memberRepository.countByOrganizationIdAndRoleNot(organizationId, role);
    }
}
