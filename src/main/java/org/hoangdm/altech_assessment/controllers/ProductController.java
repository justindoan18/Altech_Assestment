package org.hoangdm.altech_assessment.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hoangdm.altech_assessment.models.dtos.reponse.PaginationResponse;
import org.hoangdm.altech_assessment.models.dtos.reponse.ProductResponse;
import org.hoangdm.altech_assessment.models.dtos.request.CreateProductRequest;
import org.hoangdm.altech_assessment.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;
    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        PaginationResponse<ProductResponse> response = service
                .findAll(name,categoryId,minPrice,maxPrice,page,size,sortBy,sortDir);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(
            @RequestBody @Valid CreateProductRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createProduct(request));
    }

    @DeleteMapping("/{product-id}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable(name = "product-id") String productId
    ){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(service.deleteProduct(productId));
    }
}
