package com.omarcosallan.saas_api.controllers;

import com.omarcosallan.saas_api.dto.CreateOrganizationRequestDTO;
import com.omarcosallan.saas_api.dto.CreateOrganizationResponseDTO;
import com.omarcosallan.saas_api.dto.OrganizationDTO;
import com.omarcosallan.saas_api.dto.OrganizationMinDTO;
import com.omarcosallan.saas_api.services.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<CreateOrganizationResponseDTO> createOrganization(@Valid @RequestBody CreateOrganizationRequestDTO body) {
        CreateOrganizationResponseDTO organizationId = organizationService.createOrganization(body);
        return ResponseEntity.ok(organizationId);
    }

    @GetMapping(value = "/{slug}")
    public ResponseEntity<OrganizationDTO> getOrganization(@PathVariable String slug) {
        OrganizationDTO org = organizationService.getOrganization(slug);
        return ResponseEntity.ok(org);
    }

    @GetMapping()
    public ResponseEntity<List<OrganizationMinDTO>> getOrganizations() {
        List<OrganizationMinDTO> orgs = organizationService.getOrganizations();
        return ResponseEntity.ok(orgs);
    }
}