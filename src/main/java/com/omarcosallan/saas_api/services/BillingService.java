package com.omarcosallan.saas_api.services;

import com.omarcosallan.saas_api.domain.enums.Role;
import com.omarcosallan.saas_api.domain.member.Member;
import com.omarcosallan.saas_api.dto.BillingsResponseDTO;
import com.omarcosallan.saas_api.dto.ProjectsDTO;
import com.omarcosallan.saas_api.dto.SeatsDTO;
import com.omarcosallan.saas_api.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BillingService {

    @Autowired
    private MemberService memberService;

    @Autowired
    private ProjectService projectService;

    public BillingsResponseDTO getOrganizationBilling(String slug) {
        Member member = memberService.getMember(slug);

        boolean canGetBilling = member.getRole().equals(Role.ADMIN) || member.getRole().equals(Role.BILLING);
        if (!canGetBilling) {
            throw new UnauthorizedException("You're not allowed to get billing details from this organization.");
        }

        UUID organizationId = member.getOrganization().getId();

        Long amountOfMembers = memberService.countByOrganizationIdAndRoleNot(organizationId, Role.BILLING);
        Long amountOfProjects = projectService.countByOrganizationId(organizationId);

        return new BillingsResponseDTO(
                new BillingsResponseDTO.BillingDTO(
                        new SeatsDTO(amountOfMembers, 10L, amountOfMembers * 10),
                        new ProjectsDTO(amountOfProjects, 20L, amountOfProjects * 20),
                        amountOfMembers * 10 + amountOfProjects * 20
                )
        );
    }
}
