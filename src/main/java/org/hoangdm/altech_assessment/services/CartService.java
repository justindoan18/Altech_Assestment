package org.hoangdm.altech_assessment.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hoangdm.altech_assessment.exception.BusinessException;
import org.hoangdm.altech_assessment.models.entities.Cart;
import org.hoangdm.altech_assessment.models.entities.CartItem;
import org.hoangdm.altech_assessment.models.entities.Product;
import org.hoangdm.altech_assessment.repository.CartRepository;
import org.hoangdm.altech_assessment.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    public Cart createCart() {
        Cart cart = new Cart();
        cart.setId(UUID.randomUUID().toString());
        return cartRepository.save(cart);
    }

    public synchronized Cart addToCart(String cartId, String productId, int quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new BusinessException("Cart not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException("Product not found"));

        if (!product.getAvailable()) {
            throw new BusinessException("Product is not available");
        }

        // lock theo productId để tránh race condition
        synchronized (productId.intern()) {
            if (product.getStockQuantity() < quantity) {
                throw new BusinessException("Insufficient stock for product: " + product.getName());
            }

            // giảm stock
            product.setStockQuantity(product.getStockQuantity() - quantity);
            productRepository.save(product);

            // thêm vào cart
            CartItem item = new CartItem();
            item.setProductId(productId);
            item.setQuantity(quantity);
            item.setPriceAtPurchase(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
            cart.getItems().add(item);

            return cartRepository.save(cart);
        }
    }

    public synchronized Cart removeFromCart(String cartId, String productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem toRemove = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not in cart"));

        // hoàn stock lại
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        synchronized (productId.intern()) {
            product.setStockQuantity(product.getStockQuantity() + toRemove.getQuantity());
            productRepository.save(product);

            cart.getItems().remove(toRemove);
            return cartRepository.save(cart);
        }
    }

    public Cart getCart(String cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }
}
