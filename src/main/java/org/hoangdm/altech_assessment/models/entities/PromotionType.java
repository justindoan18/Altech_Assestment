package org.hoangdm.altech_assessment.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PromotionType {
    BUY_X_GET_Y_PERCENT_OFF(1,"BUY_X_GET_Y_PERCENT_OFF"),
    BUY_X_GET_DISCOUNT_PERCENT(2,"BUY_X_GET_DISCOUNT_PERCENT");

    private final int value;
    private final String type;
}