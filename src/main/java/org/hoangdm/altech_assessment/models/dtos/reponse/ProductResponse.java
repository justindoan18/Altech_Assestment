package org.hoangdm.altech_assessment.models.dtos.reponse;

import org.hoangdm.altech_assessment.models.entities.Category;

import java.math.BigDecimal;

public record ProductResponse(
    String id,
    String name,
    String description,
    BigDecimal price,
    Category category,
    int stockQuantity
) {
}
