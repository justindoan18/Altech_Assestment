package org.hoangdm.altech_assessment.repository;

import lombok.RequiredArgsConstructor;
import org.hoangdm.altech_assessment.models.entities.Category;
import org.hoangdm.altech_assessment.models.entities.Product;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ProductRepository {
    private final Map<String, Product> store = new ConcurrentHashMap<>();

    public ProductRepository() {

        // preload ~30 products
        for (int i = 1; i <= 30; i++) {
            String catId;
            if (i % 4 == 0) catId = "c1";
            else if (i % 4 == 1) catId = "c2";
            else if (i % 4 == 2) catId = "c3";
            else catId = "c4";

            Product p = new Product(
                    String.valueOf(UUID.randomUUID()),
                    "Product " + i,
                    "Description for product " + i,
                    BigDecimal.valueOf(10 + i * 2),
                    catId,
                    50 + i,
                    true // available if even
            );

            store.put(p.getId(), p);
        }
    }

    public List<Product> findAll(String name,
                                 String categoryId,
                                 Double minPrice,
                                 Double maxPrice,
                                 int page,
                                 int size,
                                 String sortBy,
                                 String sortDir) {
        return store.values().stream()
                // filter
                .filter(Product::getAvailable)
                .filter(p -> name == null || p.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(p -> categoryId == null || categoryId.equals(p.getCategoryId()))
                .filter(p -> minPrice == null || p.getPrice().doubleValue() >= minPrice)
                .filter(p -> maxPrice == null || p.getPrice().doubleValue() <= maxPrice)

                // sort
                .sorted(getComparator(sortBy, sortDir))

                // pagination
                .skip((long) page * size)
                .limit(size)
                .toList();
    }

    public long countProduct(String name,
                                 String categoryId,
                                 Double minPrice,
                                 Double maxPrice) {
        return store.values().stream()
                .filter(Product::getAvailable)
                .filter(p -> name == null || p.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(p -> categoryId == null || categoryId.equals(p.getCategoryId()))
                .filter(p -> minPrice == null || p.getPrice().doubleValue() >= minPrice)
                .filter(p -> maxPrice == null || p.getPrice().doubleValue() <= maxPrice)
                .count();
    }

    public Optional<Product> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public void removeById(String id) {
        store.remove(id);
    }

    public Product save(Product product) {
        store.put(product.getId(), product);
        return product;
    }

    private Comparator<Product> getComparator(String sortBy, String sortDir) {
        Comparator<Product> comparator = switch (sortBy) {
            case "price" -> Comparator.comparing(p -> p.getPrice().doubleValue());
            default -> Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER);
        };

        if ("desc".equalsIgnoreCase(sortDir)) {
            comparator = comparator.reversed();
        }

        return comparator;
    }
}
