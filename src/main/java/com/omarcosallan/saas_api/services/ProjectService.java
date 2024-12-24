package com.omarcosallan.saas_api.services;

import com.omarcosallan.saas_api.domain.enums.Role;
import com.omarcosallan.saas_api.domain.member.Member;
import com.omarcosallan.saas_api.domain.organization.Organization;
import com.omarcosallan.saas_api.domain.project.Project;
import com.omarcosallan.saas_api.domain.user.User;
import com.omarcosallan.saas_api.dto.CreateProjectRequestDTO;
import com.omarcosallan.saas_api.dto.CreateProjectResponseDTO;
import com.omarcosallan.saas_api.exceptions.ProjectNotFoundException;
import com.omarcosallan.saas_api.exceptions.UnauthorizedException;
import com.omarcosallan.saas_api.repositories.ProjectRepository;
import com.omarcosallan.saas_api.utils.SlugUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private UserService userService;

    @Transactional
    public CreateProjectResponseDTO createProject(String slug, @Valid CreateProjectRequestDTO body) {
        Member member = organizationService.getMember(slug);

        if (!(member.getRole() == Role.MEMBER || member.getRole() == Role.ADMIN)) {
            throw new UnauthorizedException("You're not allowed to create new projects.");
        }

        User owner = userService.authenticated();

        Project project = new Project();
        project.setName(body.name());
        project.setSlug(SlugUtils.createSlug(body.name()));
        project.setDescription(body.description());
        project.setOrganization(member.getOrganization());
        project.setOwner(owner);

        projectRepository.save(project);

        return new CreateProjectResponseDTO(project.getId());
    }

    public void deleteProject(String slug, UUID projectId) {
        Member member = organizationService.getMember(slug);
        Organization org = member.getOrganization();

        Optional<Project> project = projectRepository.findByIdAndOrganizationId(projectId, org.getId());

        if (project.isEmpty()) {
            throw new ProjectNotFoundException();
        }

        if (!(member.getRole() == Role.ADMIN
                || (member.getRole() == Role.MEMBER && project.get().getOwner().getId().equals(member.getId())))) {
            throw new UnauthorizedException("You're not allowed to delete this project.");
        }

        projectRepository.deleteById(projectId);
    }
}
