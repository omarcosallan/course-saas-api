package com.omarcosallan.saas_api.controllers;

import com.omarcosallan.saas_api.dto.CreateProjectRequestDTO;
import com.omarcosallan.saas_api.dto.CreateProjectResponseDTO;
import com.omarcosallan.saas_api.services.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}