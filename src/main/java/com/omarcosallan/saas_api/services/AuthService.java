package com.omarcosallan.saas_api.services;

import com.omarcosallan.saas_api.domain.user.User;
import com.omarcosallan.saas_api.dto.LoginResponseDTO;
import com.omarcosallan.saas_api.dto.UserMinDTO;
import com.omarcosallan.saas_api.dto.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    public LoginResponseDTO login(String email, String password) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(email, password);
        Authentication auth = authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generateToken((User) auth.getPrincipal());

        return new LoginResponseDTO(token, "7d");
    }

    public UserResponseDTO getProfile() {
        User user = userService.authenticated();
        UserMinDTO userMinDTO = new UserMinDTO(user.getId(), user.getName(), user.getEmail(), user.getAvatarUrl());
        return new UserResponseDTO(userMinDTO);
    }
}
