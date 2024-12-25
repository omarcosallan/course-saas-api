package com.omarcosallan.saas_api.dto;

public record BillingsResponseDTO(BillingDTO billings) {
    public record BillingDTO(SeatsDTO seats,
                             ProjectsDTO projects,
                             Long total) {}
}
