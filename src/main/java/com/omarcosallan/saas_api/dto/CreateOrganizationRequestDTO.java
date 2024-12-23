package com.omarcosallan.saas_api.dto;

public record CreateOrganizationRequestDTO(String name,
                                           String domain,
                                           Boolean shouldAttachUsersByDomain) {
}
