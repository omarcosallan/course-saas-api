package com.omarcosallan.saas_api.controllers;

import com.omarcosallan.saas_api.dto.LoginRequestDTO;
import com.omarcosallan.saas_api.dto.LoginResponseDTO;
import com.omarcosallan.saas_api.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/sessions/password")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO body) {
        LoginResponseDTO result = authService.login(body.email(), body.password());
        return ResponseEntity.ok(result);
    }
}
