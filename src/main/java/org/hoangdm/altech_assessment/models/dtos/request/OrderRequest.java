package org.hoangdm.altech_assessment.models.dtos.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderRequest(
        @NotEmpty(message = "Cart must not be null")
        String cartId,
        List<String> promotions
) {
}
