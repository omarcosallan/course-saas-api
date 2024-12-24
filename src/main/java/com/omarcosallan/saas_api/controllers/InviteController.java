package com.omarcosallan.saas_api.controllers;

import com.omarcosallan.saas_api.dto.CreateInviteRequestDTO;
import com.omarcosallan.saas_api.dto.CreateInviteResponseDTO;
import com.omarcosallan.saas_api.dto.InviteResponse;
import com.omarcosallan.saas_api.services.InviteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping
public class InviteController {

    @Autowired
    private InviteService inviteService;

    @PostMapping(value = "/organizations/{slug}/invites")
    public ResponseEntity<CreateInviteResponseDTO> createInvite(@PathVariable("slug") String slug, @Valid @RequestBody CreateInviteRequestDTO body) {
        CreateInviteResponseDTO result = inviteService.createInvite(slug, body);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/invites/{inviteId}")
    public ResponseEntity<InviteResponse> getInvite(@PathVariable("inviteId") UUID inviteId) {
        InviteResponse result = inviteService.getInvite(inviteId);
        return ResponseEntity.ok(result);
    }
}
