package org.hoangdm.altech_assessment.services.mapper;

import org.hoangdm.altech_assessment.exception.BusinessException;
import org.hoangdm.altech_assessment.models.entities.Promotion;
import org.hoangdm.altech_assessment.models.dtos.request.CreatePromotionRequest;
import org.hoangdm.altech_assessment.models.entities.PromotionType;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PromotionMapper {
    public Promotion toPromotion(CreatePromotionRequest request) {
        PromotionType type;
        if (request.promotionType().equals(PromotionType.BUY_X_GET_DISCOUNT_PERCENT.getValue())) {
            type = PromotionType.BUY_X_GET_DISCOUNT_PERCENT;
        } else
            if (request.promotionType().equals(PromotionType.BUY_X_GET_Y_PERCENT_OFF.getValue())) {
            type = PromotionType.BUY_X_GET_Y_PERCENT_OFF;
        } else throw new BusinessException("Invalid promotion type");

        return Promotion.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .name(request.name())
                .productId(request.productId())
                .discountPercentage(request.discountPercentage())
                .buyQuantity(request.buyQuantity())
                .discountQuantity(request.discountQuantity())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .type(type)
                .build();
    }
}
