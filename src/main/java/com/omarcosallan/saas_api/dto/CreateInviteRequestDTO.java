package com.omarcosallan.saas_api.dto;

import com.omarcosallan.saas_api.domain.enums.Role;
import jakarta.validation.constraints.Email;

public record CreateInviteRequestDTO(@Email
                                     String email,
                                     Role role) {
}
