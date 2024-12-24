package com.omarcosallan.saas_api.controllers;

import com.omarcosallan.saas_api.dto.MembersResponseDTO;
import com.omarcosallan.saas_api.services.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
