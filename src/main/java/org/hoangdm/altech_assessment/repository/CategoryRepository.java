package org.hoangdm.altech_assessment.repository;

import org.hoangdm.altech_assessment.models.entities.Category;
import org.hoangdm.altech_assessment.models.entities.Product;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CategoryRepository {
    private final Map<String, Category> categoryMap = new ConcurrentHashMap<>();
    public CategoryRepository() {
        // fake categories
        categoryMap.put("c1", new Category("c1", "Electronics"));
        categoryMap.put("c2", new Category("c2", "Fashion"));
        categoryMap.put("c3", new Category("c3", "Books"));
        categoryMap.put("c4", new Category("c4", "Groceries"));
    }
    public Optional<Category> findById(String id) {
        return Optional.ofNullable(categoryMap.get(id));
    }
}
