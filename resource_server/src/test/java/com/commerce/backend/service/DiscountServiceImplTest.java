package com.commerce.backend.service;

import com.commerce.backend.dao.DiscountRepository;
import com.commerce.backend.error.exception.InvalidArgumentException;
import com.commerce.backend.error.exception.ResourceNotFoundException;
import com.commerce.backend.model.entity.Cart;
import com.commerce.backend.model.entity.Discount;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class DiscountServiceImplTest {

    @InjectMocks
    private DiscountServiceImpl discountService;

    @Mock
    private DiscountRepository discountRepository;

    @Mock
    private CartService cartService;

    private Faker faker;


    @BeforeEach
    public void setUp() {
        faker = new Faker();
    }

    @Test
    void it_should_apply_discount() {

        // given
        String code = faker.random().hex();

        Discount discount = new Discount();
        discount.setStatus(1);
        Cart cart = new Cart();

        ArgumentCaptor<Cart> cartArgumentCaptor = ArgumentCaptor.forClass(Cart.class);

        given(discountRepository.findByCode(code)).willReturn(Optional.of(discount));
        given(cartService.getCart()).willReturn(cart);
        given(cartService.calculatePrice(cart)).willReturn(cart);

        // when
        discountService.applyDiscount(code);

        // then
        verify(cartService).saveCart(cartArgumentCaptor.capture());
        then(cart).isEqualTo(cartArgumentCaptor.getValue());

    }

    @Test
    void it_should_throw_exception_when_invalid_code() {

        // given
        String code = faker.random().hex();

        given(discountRepository.findByCode(code)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> discountService.applyDiscount(code))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Discount code not found");

    }

    @Test
    void it_should_throw_exception_when_expired_code() {

        // given
        String code = faker.random().hex();

        Discount discount = new Discount();
        discount.setStatus(0);

        given(discountRepository.findByCode(code)).willReturn(Optional.of(discount));

        // when, then
        assertThatThrownBy(() -> discountService.applyDiscount(code))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessage("Discount code is expired!");

    }
}