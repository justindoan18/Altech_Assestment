package org.hoangdm.altech_assessment.models.dtos.request;

import java.time.LocalDate;

public record UpdatePromotionRequest(
        LocalDate startDate,
        LocalDate endDate
) {
}
