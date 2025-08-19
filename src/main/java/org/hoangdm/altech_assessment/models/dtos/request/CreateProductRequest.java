package org.hoangdm.altech_assessment.models.dtos.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hoangdm.altech_assessment.models.entities.Category;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotEmpty(message = "Product name is required")
        String name,
        String description,
        @NotNull(message = "Price is required")
        @Positive(message = "Price must greater or equals 0")
        BigDecimal price,
        @NotEmpty(message = "categoryId is required")
        String categoryId,
        @Positive(message = "Stock quantity must greater or equals 0")
        Integer stockQuantity,
        Boolean available
) {
}
