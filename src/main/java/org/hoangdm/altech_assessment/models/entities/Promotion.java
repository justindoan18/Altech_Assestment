package org.hoangdm.altech_assessment.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Promotion {
    private String id;
    private String name;
    private String productId;
    private PromotionType type;
    private Integer buyQuantity;
    private Integer discountQuantity;
    private BigDecimal discountPercentage; // 0.5 = 50%
    private LocalDate startDate;
    private LocalDate endDate;
}
