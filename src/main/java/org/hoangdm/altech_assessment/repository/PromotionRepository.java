package org.hoangdm.altech_assessment.repository;

import org.hoangdm.altech_assessment.models.entities.Promotion;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class PromotionRepository {
    private final Map<String, Promotion> store = new ConcurrentHashMap<>();

    public Promotion save(Promotion promotion) {
        store.put(promotion.getId(), promotion);
        return promotion;
    }

    public Optional<Promotion> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Promotion> findAll() {
        return new ArrayList<>(store.values());
    }
}
