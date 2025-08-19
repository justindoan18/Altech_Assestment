package org.hoangdm.altech_assessment.services;

import lombok.RequiredArgsConstructor;
import org.hoangdm.altech_assessment.constants.ResponseConstant;
import org.hoangdm.altech_assessment.exception.BusinessException;
import org.hoangdm.altech_assessment.models.dtos.reponse.ResponseBaseSingle;
import org.hoangdm.altech_assessment.models.entities.Cart;
import org.hoangdm.altech_assessment.models.entities.Order;
import org.hoangdm.altech_assessment.models.entities.OrderItem;
import org.hoangdm.altech_assessment.models.entities.Promotion;
import org.hoangdm.altech_assessment.repository.CartRepository;
import org.hoangdm.altech_assessment.repository.PromotionRepository;
import org.hoangdm.altech_assessment.services.mapper.OrderMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderService {
    private final OrderMapper mapper;
    private final CartRepository cartRepository;
    private final PromotionRepository promotionRepository;
    public ResponseBaseSingle<Order> calculateOrder(String cartId, List<String> promotionIds) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new BusinessException("Invalid cart id"));

        List<OrderItem> orderItems = mapper.fromCartToOrderItem(cart);

        List<Promotion> promotions = promotionIds.stream()
                .map(n -> promotionRepository.findById(n)
                        .orElseThrow(() -> new BusinessException("Promotion code " + n + " is not found"))).toList();

        for (OrderItem item : orderItems) {
            promotions.stream()
                    .filter(promo -> promo.getProductId().equals(item.getProductId()))
                    .forEach(promo -> applyPromotion(item, promo));
        }

        return new ResponseBaseSingle<>(
                HttpStatus.OK.value(),
                ResponseConstant.SUCCESS_MESSAGE,
                mapper.fromOrderItem(orderItems)
        );
    }
    public void applyPromotion(OrderItem item, Promotion promotion) {
        if (!promotion.getProductId().equals(item.getProductId())) {
            return; // promotion không áp dụng cho product này
        }

        if (LocalDate.now().isBefore(promotion.getStartDate()) ||
                LocalDate.now().isAfter(promotion.getEndDate())) {
            return; // chưa tới ngày / hết hạn
        }

        switch (promotion.getType()) {
            case BUY_X_GET_Y_PERCENT_OFF -> applyBuyXGetYPercentOff(item, promotion);
            case BUY_X_GET_DISCOUNT_PERCENT -> applyBuyXGetDiscountPercent(item, promotion);
        }
    }

    private void applyBuyXGetYPercentOff(OrderItem item, Promotion promotion) {
        int buyQty = promotion.getBuyQuantity();
        int discountQty = promotion.getDiscountQuantity();
        BigDecimal discountPercent = promotion.getDiscountPercentage();

        if (item.getQuantity() < buyQty + discountQty) return;

        int eligibleSets = item.getQuantity() / (buyQty + discountQty);
        BigDecimal discount = item.getUnitPrice()
                .multiply(discountPercent)
                .multiply(BigDecimal.valueOf(eligibleSets * discountQty));

        item.setDiscountApplied(discount);
        item.setFinalPrice(item.getFinalPrice().subtract(discount));
    }

    private void applyBuyXGetDiscountPercent(OrderItem item, Promotion promotion) {
        int buyQty = promotion.getBuyQuantity();
        BigDecimal discountPercent = promotion.getDiscountPercentage();

        if (item.getQuantity() < buyQty) return;

        BigDecimal discount = item.getUnitPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity()))
                .multiply(discountPercent);

        item.setDiscountApplied(discount);
        item.setFinalPrice(item.getFinalPrice().subtract(discount));
    }
}
