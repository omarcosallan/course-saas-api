package com.omarcosallan.saas_api.controllers;

import com.omarcosallan.saas_api.dto.*;
import com.omarcosallan.saas_api.services.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<OrganizationDTO> getOrganization(@PathVariable("slug") String slug) {
        OrganizationDTO org = organizationService.getOrganization(slug);
        return ResponseEntity.ok(org);
    }

    @GetMapping
    public ResponseEntity<List<OrganizationMinDTO>> getOrganizations() {
        List<OrganizationMinDTO> orgs = organizationService.getOrganizations();
        return ResponseEntity.ok(orgs);
    }

    @PutMapping(value = "/{slug}")
    public ResponseEntity<Void> updateOrganization(@PathVariable("slug") String slug, @Valid @RequestBody UpdateOrganizationRequestDTO body) {
        organizationService.updateOrganization(slug, body);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(value = "/{slug}")
    public ResponseEntity<Void> shutdownOrganization(@PathVariable("slug") String slug) {
        organizationService.shutdownOrganization(slug);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping(value = "/{slug}/owner")
    public ResponseEntity<Void> transferOrganization(@PathVariable("slug") String slug, @Valid @RequestBody TransferOrganizationRequestDTO body) {
        organizationService.transferOrganization(slug, body);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
