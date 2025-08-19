package org.hoangdm.altech_assessment.services.mapper;

import org.hoangdm.altech_assessment.models.entities.Category;
import org.hoangdm.altech_assessment.models.entities.Product;
import org.hoangdm.altech_assessment.models.dtos.reponse.ProductResponse;
import org.hoangdm.altech_assessment.models.dtos.request.CreateProductRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductMapper {

    public ProductResponse toProductResponse(Product product, Category category) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                category,
                product.getStockQuantity()
        );
    }

    public Product toProduct(CreateProductRequest request) {
        return Product.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .categoryId(request.categoryId())
                .stockQuantity(request.stockQuantity() != null ? request.stockQuantity() : 0)
                .available(request.available() != null ? request.available() : true)
                .build();
    }


}
