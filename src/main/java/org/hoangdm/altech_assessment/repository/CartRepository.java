package org.hoangdm.altech_assessment.repository;

import org.hoangdm.altech_assessment.models.entities.Cart;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CartRepository {
    private final Map<String, Cart> carts = new ConcurrentHashMap<>();

    public synchronized Cart save(Cart cart) {
        carts.put(cart.getId(), cart);
        return cart;
    }

    public Optional<Cart> findById(String id) {
        return Optional.ofNullable(carts.get(id));
    }

    public synchronized void deleteById(String id) {
        carts.remove(id);
    }
}
