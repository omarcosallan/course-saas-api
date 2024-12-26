package com.omarcosallan.saas_api.services;

import com.omarcosallan.saas_api.domain.user.User;
import com.omarcosallan.saas_api.dto.CreateUserDTO;
import com.omarcosallan.saas_api.dto.UserMinDTO;
import com.omarcosallan.saas_api.dto.UserResponseDTO;
import com.omarcosallan.saas_api.exceptions.UserAlreadyExistsException;
import com.omarcosallan.saas_api.exceptions.UserNotFoundException;
import com.omarcosallan.saas_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public User create(CreateUserDTO body) {
        Optional<User> userAlreadyExists =  userRepository.findByEmail(body.email());

        if (userAlreadyExists.isPresent()) {
            throw new UserAlreadyExistsException();
        }

        String passwordHash = passwordEncoder.encode(body.password());

        User user = new User();
        user.setName(body.name());
        user.setEmail(body.email());
        user.setPasswordHash(passwordHash);


        return userRepository.save(user);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }

    public User authenticated() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(userEmail)
                .orElseThrow(UserNotFoundException::new);
    }
}
