package org.hoangdm.altech_assessment.controllers;

import lombok.RequiredArgsConstructor;
import org.hoangdm.altech_assessment.models.dtos.request.OrderRequest;
import org.hoangdm.altech_assessment.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/checkout")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;

    @PostMapping("/calculate")
    public ResponseEntity<?> calculateOrder(@RequestBody OrderRequest request) {
        return ResponseEntity.ok(service.calculateOrder(request.cartId(), request.promotions()));
    }
}
