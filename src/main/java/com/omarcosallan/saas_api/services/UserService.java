package com.omarcosallan.saas_api.services;

import com.omarcosallan.saas_api.domain.user.User;
import com.omarcosallan.saas_api.dto.CreateUserDTO;
import com.omarcosallan.saas_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User create(CreateUserDTO body) {
        Optional<User> userAlreadyExists =  userRepository.findByEmail(body.email());

        if (userAlreadyExists.isPresent()) {
            throw new RuntimeException("User with same e-mail already exists.");
        }

        String passwordHash = passwordEncoder.encode(body.password());

        User user = new User();
        user.setName(body.name());
        user.setEmail(body.email());
        user.setPasswordHash(passwordHash);

        return userRepository.save(user);
    }
}
