package org.hoangdm.altech_assessment.services.mapper;

import org.hoangdm.altech_assessment.models.entities.Cart;
import org.hoangdm.altech_assessment.models.entities.CartItem;
import org.hoangdm.altech_assessment.models.entities.Order;
import org.hoangdm.altech_assessment.models.entities.OrderItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderMapper {
    public List<OrderItem> fromCartToOrderItem(Cart cart) {
        return cart.getItems().stream()
                .map(this::mapCartItemToOrderItem)
                .collect(Collectors.toList());
    }

    private OrderItem mapCartItemToOrderItem(CartItem cartItem) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(cartItem.getProductId());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setUnitPrice(cartItem.getPriceAtPurchase());
        orderItem.setDiscountApplied(BigDecimal.ZERO);
        orderItem.setFinalPrice(cartItem.getPriceAtPurchase()
                .multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        return orderItem;
    }

    public Order fromOrderItem(List<OrderItem> orderItems) {
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderItem item : orderItems) {
            totalPrice = totalPrice.add(item.getFinalPrice());
        }

        Order order = Order.builder()
                .items(orderItems)
                .totalPrice(totalPrice)
                .build();
    }
}
