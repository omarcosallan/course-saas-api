package com.omarcosallan.saas_api.controllers;

import com.omarcosallan.saas_api.dto.BillingsResponseDTO;
import com.omarcosallan.saas_api.services.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class BillingController {

    @Autowired
    private BillingService billingService;

    @GetMapping(value = "/organizations/{slug}/billing")
    public ResponseEntity<BillingsResponseDTO> getOrganizationBilling(@PathVariable("slug") String slug) {
        BillingsResponseDTO result = billingService.getOrganizationBilling(slug);
        return ResponseEntity.ok(result);
    }
}
