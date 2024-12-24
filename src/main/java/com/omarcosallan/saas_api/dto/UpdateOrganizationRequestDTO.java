package com.omarcosallan.saas_api.dto;

public record UpdateOrganizationRequestDTO(String name,
                                           String domain,
                                           Boolean shouldAttachUsersByDomain) {
}
