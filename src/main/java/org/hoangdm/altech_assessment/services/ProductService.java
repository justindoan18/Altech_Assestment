package org.hoangdm.altech_assessment.services;

import lombok.RequiredArgsConstructor;
import org.hoangdm.altech_assessment.constants.ResponseConstant;
import org.hoangdm.altech_assessment.exception.BusinessException;
import org.hoangdm.altech_assessment.models.entities.Category;
import org.hoangdm.altech_assessment.models.entities.Product;
import org.hoangdm.altech_assessment.models.dtos.reponse.PaginationResponse;
import org.hoangdm.altech_assessment.models.dtos.reponse.ProductResponse;
import org.hoangdm.altech_assessment.models.dtos.reponse.ResponseBaseSingle;
import org.hoangdm.altech_assessment.models.dtos.request.CreateProductRequest;
import org.hoangdm.altech_assessment.repository.CategoryRepository;
import org.hoangdm.altech_assessment.repository.ProductRepository;
import org.hoangdm.altech_assessment.services.mapper.ProductMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper mapper;
    public PaginationResponse<ProductResponse> findAll(String name,
                                        String categoryId,
                                        Double minPrice,
                                        Double maxPrice,
                                        int page,
                                        int size,
                                        String sortBy,
                                        String sortDir) {

        List<ProductResponse> products = productRepository
                .findAll(name,categoryId,minPrice,maxPrice,page,size,sortBy,sortDir)
                .stream()
                .map(n -> {
                    Optional<Category> cat = categoryRepository.findById(n.getCategoryId());
                    return mapper.toProductResponse(n, cat.get());
                })
                .collect(Collectors.toList());

        int totalElement = (int) productRepository.countProduct(name,categoryId,minPrice,maxPrice);
        int totalPage = (int) Math.ceil((double) totalElement/size);
        page = Math.max(1, Math.min(page, totalPage));

        PaginationResponse<ProductResponse> result = new PaginationResponse<ProductResponse>(
                HttpStatus.OK.value(),
                ResponseConstant.SUCCESS_MESSAGE,
                products,
                page,
                size,
                totalPage,
                totalElement
        );

        return result;
    }

    public ResponseBaseSingle<Product> createProduct(CreateProductRequest request) {
        if (categoryRepository.findById(request.categoryId()).isEmpty()) {
            throw new BusinessException("Category not found");
        }
        Product product = mapper.toProduct(request);
        productRepository.save(mapper.toProduct(request));
        return new ResponseBaseSingle<>(
                HttpStatus.OK.value(),
                ResponseConstant.SUCCESS_MESSAGE,
                product
        );
    }

    public ResponseBaseSingle<String> deleteProduct(String productId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException("Product id not found"));

        productRepository.removeById(productId);

        return new ResponseBaseSingle<>(
                HttpStatus.OK.value(),
                ResponseConstant.SUCCESS_MESSAGE,
                "Product id " + productId + "has been deleted"
        );

    }
}
