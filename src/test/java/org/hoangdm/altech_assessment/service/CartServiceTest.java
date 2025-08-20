package org.hoangdm.altech_assessment.service;

import org.hoangdm.altech_assessment.constants.ResponseConstant;
import org.hoangdm.altech_assessment.exception.BusinessException;
import org.hoangdm.altech_assessment.models.dtos.reponse.ResponseBaseSingle;
import org.hoangdm.altech_assessment.models.entities.Cart;
import org.hoangdm.altech_assessment.models.entities.CartItem;
import org.hoangdm.altech_assessment.models.entities.Product;
import org.hoangdm.altech_assessment.repository.CartRepository;
import org.hoangdm.altech_assessment.repository.ProductRepository;
import org.hoangdm.altech_assessment.services.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCart_ShouldReturnSavedCart() {
        Cart cart = new Cart();
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.createCart();

        assertNotNull(result);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Nested
    class AddToCartTests {
        private Cart cart;
        private Product product;

        @BeforeEach
        void init() {
            cart = new Cart();
            cart.setId(UUID.randomUUID().toString());

            product = new Product();
            product.setId("p1");
            product.setName("Product 1");
            product.setAvailable(true);
            product.setStockQuantity(10);
            product.setPrice(BigDecimal.valueOf(100));
        }

        @Test
        void cartNotFound_ShouldThrowException() {
            when(cartRepository.findById("c1")).thenReturn(Optional.empty());

            assertThrows(BusinessException.class,
                    () -> cartService.addToCart("c1", "p1", 1));
        }

        @Test
        void productNotFound_ShouldThrowException() {
            when(cartRepository.findById("c1")).thenReturn(Optional.of(cart));
            when(productRepository.findById("p1")).thenReturn(Optional.empty());

            assertThrows(BusinessException.class,
                    () -> cartService.addToCart("c1", "p1", 1));
        }

        @Test
        void productNotAvailable_ShouldThrowException() {
            product.setAvailable(false);
            when(cartRepository.findById("c1")).thenReturn(Optional.of(cart));
            when(productRepository.findById("p1")).thenReturn(Optional.of(product));

            assertThrows(BusinessException.class,
                    () -> cartService.addToCart("c1", "p1", 1));
        }

        @Test
        void insufficientStock_ShouldThrowException() {
            product.setStockQuantity(0);
            when(cartRepository.findById("c1")).thenReturn(Optional.of(cart));
            when(productRepository.findById("p1")).thenReturn(Optional.of(product));

            assertThrows(BusinessException.class,
                    () -> cartService.addToCart("c1", "p1", 1));
        }

        @Test
        void success_ShouldAddItemAndReduceStock() {
            when(cartRepository.findById("c1")).thenReturn(Optional.of(cart));
            when(productRepository.findById("p1")).thenReturn(Optional.of(product));
            when(productRepository.save(any(Product.class))).thenReturn(product);
            when(cartRepository.save(any(Cart.class))).thenReturn(cart);

            ResponseBaseSingle<Cart> response =
                    cartService.addToCart("c1", "p1", 2);

            assertEquals(HttpStatus.OK.value(), response.code());
            assertEquals(ResponseConstant.SUCCESS_MESSAGE, response.message());
            assertEquals(8, product.getStockQuantity()); // giảm stock
            assertFalse(cart.getItems().isEmpty());

            CartItem addedItem = cart.getItems().get(0);
            assertEquals("p1", addedItem.getProductId());
            assertEquals(2, addedItem.getQuantity());
            assertEquals(BigDecimal.valueOf(200), addedItem.getPriceAtPurchase());
        }
    }

    @Nested
    class RemoveFromCartTests {
        private Cart cart;
        private Product product;

        @BeforeEach
        void init() {
            product = new Product();
            product.setId("p1");
            product.setName("Product 1");
            product.setAvailable(true);
            product.setStockQuantity(5);
            product.setPrice(BigDecimal.valueOf(100));

            CartItem item = new CartItem();
            item.setProductId("p1");
            item.setQuantity(2);
            item.setPriceAtPurchase(BigDecimal.valueOf(200));

            cart = new Cart();
            cart.setId("c1");
            cart.getItems().add(item);
        }

        @Test
        void cartNotFound_ShouldThrowException() {
            when(cartRepository.findById("c1")).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class,
                    () -> cartService.removeFromCart("c1", "p1"));
        }

        @Test
        void productNotInCart_ShouldThrowException() {
            cart.getItems().clear();
            when(cartRepository.findById("c1")).thenReturn(Optional.of(cart));

            assertThrows(RuntimeException.class,
                    () -> cartService.removeFromCart("c1", "p1"));
        }

        @Test
        void success_ShouldRemoveItemAndRestoreStock() {
            when(cartRepository.findById("c1")).thenReturn(Optional.of(cart));
            when(productRepository.findById("p1")).thenReturn(Optional.of(product));
            when(cartRepository.save(any(Cart.class))).thenReturn(cart);
            when(productRepository.save(any(Product.class))).thenReturn(product);

            ResponseBaseSingle<Cart> response =
                    cartService.removeFromCart("c1", "p1");

            assertEquals(HttpStatus.OK.value(), response.code());
            assertEquals(ResponseConstant.SUCCESS_MESSAGE, response.message());
            assertTrue(cart.getItems().isEmpty());
            assertEquals(7, product.getStockQuantity()); // stock được hoàn lại
        }
    }

    @Nested
    class GetCartTests {
        @Test
        void cartFound_ShouldReturnCart() {
            Cart cart = new Cart();
            cart.setId("c1");
            when(cartRepository.findById("c1")).thenReturn(Optional.of(cart));

            Cart result = cartService.getCart("c1");

            assertEquals("c1", result.getId());
        }

        @Test
        void cartNotFound_ShouldThrowException() {
            when(cartRepository.findById("c1")).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class,
                    () -> cartService.getCart("c1"));
        }
    }
}
