package com.omarcosallan.saas_api.controllers;

import com.omarcosallan.saas_api.domain.user.User;
import com.omarcosallan.saas_api.dto.CreateUserDTO;
import com.omarcosallan.saas_api.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody CreateUserDTO body) {
        User result = userService.create(body);
        return ResponseEntity.ok(result);
    }
}
