package com.omarcosallan.saas_api.controllers;

import com.omarcosallan.saas_api.dto.CreateInviteRequestDTO;
import com.omarcosallan.saas_api.dto.CreateInviteResponseDTO;
import com.omarcosallan.saas_api.services.InviteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/organizations/{slug}/invites")
public class InviteController {

    @Autowired
    private InviteService inviteService;

    @PostMapping
    public ResponseEntity<CreateInviteResponseDTO> createInvite(@PathVariable String slug, @Valid @RequestBody CreateInviteRequestDTO body) {
        CreateInviteResponseDTO result = inviteService.createInvite(slug, body);
        return ResponseEntity.ok(result);
    }
}
