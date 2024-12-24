package com.omarcosallan.saas_api.controllers;

import com.omarcosallan.saas_api.dto.CreateProjectRequestDTO;
import com.omarcosallan.saas_api.dto.CreateProjectResponseDTO;
import com.omarcosallan.saas_api.dto.ProjectResponseDTO;
import com.omarcosallan.saas_api.dto.ProjectsResponseDTO;
import com.omarcosallan.saas_api.services.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/organizations/{slug}/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public ResponseEntity<CreateProjectResponseDTO> createProject(@PathVariable("slug") String slug, @Valid @RequestBody CreateProjectRequestDTO body) {
        CreateProjectResponseDTO result = projectService.createProject(slug, body);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(value = "/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable("slug") String slug, @PathVariable("projectId") UUID projectId) {
        projectService.deleteProject(slug, projectId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{projectSlug}")
    public ResponseEntity<ProjectResponseDTO> getProject(@PathVariable("slug") String slug, @PathVariable("projectSlug") String projectSlug) {
        ProjectResponseDTO result = projectService.getProject(slug, projectSlug);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<ProjectsResponseDTO> getProjects(@PathVariable("slug") String slug) {
        ProjectsResponseDTO result = projectService.getProjects(slug);
        return ResponseEntity.ok(result);
    }
}
