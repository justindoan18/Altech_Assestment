package org.hoangdm.altech_assessment.models.dtos.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hoangdm.altech_assessment.models.entities.PromotionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreatePromotionRequest(
         String name,
         @NotEmpty(message = "Product id must not null")
         String productId,
         @Positive(message = "Buy quantity must greater than 0")
         Integer buyQuantity,
         @Positive(message = "Discount quantity must greater than 0")
         Integer discountQuantity,
         @NotNull(message = "Discount percentage must not null")
         @Positive(message = "Discount percentage must greater than 0")
         BigDecimal discountPercentage,
         @NotNull(message = "Start date must not null")
         LocalDate startDate,
         @NotNull(message = "End date must not null")
         LocalDate endDate,

         Integer promotionType
) {
}
