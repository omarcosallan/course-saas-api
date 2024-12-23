package com.omarcosallan.saas_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record LoginRequestDTO(@NotNull
                       @Email
                       String email,
                              @NotNull
                       String password) {
}
