package org.hoangdm.altech_assessment.controllers;

import lombok.RequiredArgsConstructor;
import org.hoangdm.altech_assessment.services.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<?> createCart() {
        return ResponseEntity.ok(cartService.createCart());
    }

    @PostMapping("/{cartId}/add")
    public ResponseEntity<?> addToCart(
            @PathVariable String cartId,
            @RequestParam String productId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.addToCart(cartId, productId, quantity));
    }

    @DeleteMapping("/{cartId}/remove")
    public ResponseEntity<?> removeFromCart(
            @PathVariable String cartId,
            @RequestParam String productId) {
        return ResponseEntity.ok(cartService.removeFromCart(cartId, productId));
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<?> getCart(@PathVariable String cartId) {
        return ResponseEntity.ok(cartService.getCart(cartId));
    }
}
