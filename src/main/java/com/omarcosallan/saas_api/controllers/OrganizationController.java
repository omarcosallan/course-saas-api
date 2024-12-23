package com.omarcosallan.saas_api.controllers;

import com.omarcosallan.saas_api.dto.CreateOrganizationRequestDTO;
import com.omarcosallan.saas_api.dto.CreateOrganizationResponseDTO;
import com.omarcosallan.saas_api.services.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "/organization")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<CreateOrganizationResponseDTO> createOrganization(@Valid @RequestBody CreateOrganizationRequestDTO body) {
        CreateOrganizationResponseDTO organizationId = organizationService.createOrganization(body);
        return ResponseEntity.ok(organizationId);
    }
}
