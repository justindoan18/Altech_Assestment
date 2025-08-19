package org.hoangdm.altech_assessment.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private String productId;
    private int quantity;
    private BigDecimal unitPrice;

    // kết quả sau khi apply deal
    private BigDecimal discountApplied;
    private BigDecimal finalPrice;
}
