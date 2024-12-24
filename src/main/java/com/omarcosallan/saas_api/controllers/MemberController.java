package com.omarcosallan.saas_api.controllers;

import com.omarcosallan.saas_api.dto.MembersResponseDTO;
import com.omarcosallan.saas_api.dto.UpdateMemberRequestDTO;
import com.omarcosallan.saas_api.services.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/organizations/{slug}/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping
    public ResponseEntity<MembersResponseDTO> getMembers(@PathVariable("slug") String slug) {
        MembersResponseDTO result = memberService.getMembers(slug);
        return ResponseEntity.ok(result);
    }

    @PutMapping(value = "/{memberId}")
    public ResponseEntity<Void> updateMember(@PathVariable("slug") String slug, @PathVariable("memberId") UUID memberId, @Valid @RequestBody UpdateMemberRequestDTO body) {
        memberService.updateMember(slug, memberId, body);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{memberId}")
    public ResponseEntity<Void> removeMember(@PathVariable("slug") String slug, @PathVariable("memberId") UUID memberId) {
        memberService.removeMember(slug, memberId);
        return ResponseEntity.noContent().build();
    }
}
