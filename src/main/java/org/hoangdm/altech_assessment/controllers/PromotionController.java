package org.hoangdm.altech_assessment.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hoangdm.altech_assessment.models.dtos.request.UpdatePromotionRequest;
import org.hoangdm.altech_assessment.models.entities.Promotion;
import org.hoangdm.altech_assessment.models.dtos.request.CreatePromotionRequest;
import org.hoangdm.altech_assessment.services.PromotionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/v1/promotion")
@RequiredArgsConstructor
public class PromotionController {
    private final PromotionService service;

    @PostMapping
    public ResponseEntity<?> createPromotion(
            @RequestBody @Valid CreatePromotionRequest request
    ) {
        var response = service.createPromotion(request);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}/dates")
    public ResponseEntity<?> updatePromotionDates(
            @PathVariable String id,
            @RequestBody UpdatePromotionRequest request) {

        return ResponseEntity.ok(service.updatePromotion(id, request));
    }
}
