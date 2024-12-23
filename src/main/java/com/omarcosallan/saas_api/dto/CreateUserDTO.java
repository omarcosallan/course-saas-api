package com.omarcosallan.saas_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CreateUserDTO(@NotNull
                            String name,
                            @NotNull
                            @Email
                            String email,
                            @NotNull
                            String password) {
}
