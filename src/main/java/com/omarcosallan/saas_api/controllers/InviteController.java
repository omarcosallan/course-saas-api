package com.omarcosallan.saas_api.controllers;

import com.omarcosallan.saas_api.dto.*;
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
    public ResponseEntity<InviteResponseDTO> getInvite(@PathVariable("inviteId") UUID inviteId) {
        InviteResponseDTO result = inviteService.getInvite(inviteId);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/organizations/{slug}/invites")
    public ResponseEntity<InvitesMinResponseDTO> getInvites(@PathVariable("slug") String slug) {
        InvitesMinResponseDTO result = inviteService.getInvites(slug);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/invites/{inviteId}/accept")
    public ResponseEntity<Void> acceptInvite(@PathVariable("inviteId") UUID inviteId) {
        inviteService.acceptInvite(inviteId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/invites/{inviteId}/reject")
    public ResponseEntity<Void> rejectInvite(@PathVariable("inviteId") UUID inviteId) {
        inviteService.rejectInvite(inviteId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/organizations/{slug}/invites/{inviteId}")
    public ResponseEntity<Void> revokeInvite(@PathVariable("slug") String slug, @PathVariable("inviteId") UUID inviteId) {
        inviteService.revokeInvite(slug, inviteId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/pending-invites")
    public ResponseEntity<InvitesResponseDTO> getPendingInvites() {
        InvitesResponseDTO result = inviteService.getPendingInvites();
        return ResponseEntity.ok(result);
    }
}
