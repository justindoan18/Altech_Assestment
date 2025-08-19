package org.hoangdm.altech_assessment.services;

import lombok.RequiredArgsConstructor;
import org.hoangdm.altech_assessment.constants.ResponseConstant;
import org.hoangdm.altech_assessment.exception.BusinessException;
import org.hoangdm.altech_assessment.models.dtos.request.UpdatePromotionRequest;
import org.hoangdm.altech_assessment.models.entities.OrderItem;
import org.hoangdm.altech_assessment.models.entities.Promotion;
import org.hoangdm.altech_assessment.models.dtos.reponse.ResponseBaseSingle;
import org.hoangdm.altech_assessment.models.dtos.request.CreatePromotionRequest;
import org.hoangdm.altech_assessment.repository.ProductRepository;
import org.hoangdm.altech_assessment.repository.PromotionRepository;
import org.hoangdm.altech_assessment.services.mapper.PromotionMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PromotionService {
    private final PromotionRepository promotionRepository;
    private final ProductRepository productRepository;
    private final PromotionMapper mapper;
    public ResponseBaseSingle<Promotion> createPromotion(CreatePromotionRequest request) {
        var product = productRepository.findById(request.productId())
                .orElseThrow(() -> new BusinessException("Product not exist"));

        Promotion promotion = promotionRepository.save(mapper.toPromotion(request));

        return new ResponseBaseSingle<>(
                HttpStatus.CREATED.value(),
                ResponseConstant.SUCCESS_MESSAGE,
                promotion
        );
    }

    public ResponseBaseSingle<Promotion> updatePromotion(String id, UpdatePromotionRequest request) {
        Promotion promotion = promotionRepository.findById(id).map(promo -> {
            promo.setStartDate(request.startDate());
            promo.setEndDate(request.endDate());
            promotionRepository.save(promo);
            return promo;
        }).orElseThrow(() -> new BusinessException("Promotion not found"));

        return new ResponseBaseSingle<>(
                HttpStatus.OK.value(),
                ResponseConstant.SUCCESS_MESSAGE,
                promotion
        );
    }
}
