package com.commerce.backend.api;

import com.commerce.backend.model.dto.DiscountDTO;
import com.commerce.backend.model.request.cart.*;
import com.commerce.backend.model.response.cart.CartResponse;
import com.commerce.backend.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
@WebMvcTest(CartController.class)
@AutoConfigureWebClient
@WithMockUser
@ComponentScan(basePackages = {"com.commerce.backend.constants"})
class CartControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private CartService cartService;
    @Autowired
    private MockMvc mockMvc;
    private Faker faker;

    @BeforeEach
    public void setUp() {
        faker = new Faker();
    }

    @Test
    void it_should_add_to_cart() throws Exception {

        // given
        AddToCartRequest addToCartRequest = new AddToCartRequest();
        addToCartRequest.setAmount(faker.number().randomDigitNotZero());
        addToCartRequest.setProductVariantId(faker.number().randomNumber());

        CartResponse cartResponseExpected = new CartResponse();

        given(cartService.addToCart(addToCartRequest.getProductVariantId(), addToCartRequest.getAmount())).willReturn(cartResponseExpected);

        // when
        MvcResult result = mockMvc.perform(post("/api/cart")
                .content(objectMapper.writeValueAsString(addToCartRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // then
        verify(cartService, times(1)).addToCart(addToCartRequest.getProductVariantId(), addToCartRequest.getAmount());
        then(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(cartResponseExpected));
    }

    @Test
    void it_should_not_add_to_cart_when_amount_is_invalid() throws Exception {

        // given
        AddToCartRequest addToCartRequest = new AddToCartRequest();
        addToCartRequest.setAmount(null);
        addToCartRequest.setProductVariantId(faker.number().randomNumber());

        // when
        MvcResult result = mockMvc.perform(post("/api/cart")
                .content(objectMapper.writeValueAsString(addToCartRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        // then
        then(result.getResponse().getContentAsString()).contains("must not be null");

    }

    @Test
    void it_should_not_add_to_cart_when_product_variant_id_is_invalid() throws Exception {

        // given
        AddToCartRequest addToCartRequest = new AddToCartRequest();
        addToCartRequest.setAmount(faker.number().randomDigitNotZero());
        addToCartRequest.setProductVariantId(null);

        // when
        MvcResult result = mockMvc.perform(post("/api/cart")
                .content(objectMapper.writeValueAsString(addToCartRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        // then
        then(result.getResponse().getContentAsString()).contains("must not be null");

    }

    @Test
    void it_should_increase_cart_item() throws Exception {

        // given
        IncrementCartItemRequest incrementCartItemRequest = new IncrementCartItemRequest();
        incrementCartItemRequest.setAmount(faker.number().randomDigitNotZero());
        incrementCartItemRequest.setCartItemId(faker.number().randomNumber());

        CartResponse cartResponseExpected = new CartResponse();

        given(cartService.incrementCartItem(incrementCartItemRequest.getCartItemId(), incrementCartItemRequest.getAmount())).willReturn(cartResponseExpected);

        // when
        MvcResult result = mockMvc.perform(post("/api/cart/increment")
                .content(objectMapper.writeValueAsString(incrementCartItemRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // then
        verify(cartService, times(1)).incrementCartItem(incrementCartItemRequest.getCartItemId(), incrementCartItemRequest.getAmount());
        then(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(cartResponseExpected));
    }

    @Test
    void it_should_not_increment_cart_item_when_amount_is_invalid() throws Exception {

        // given
        IncrementCartItemRequest incrementCartItemRequest = new IncrementCartItemRequest();
        incrementCartItemRequest.setAmount(null);
        incrementCartItemRequest.setCartItemId(faker.number().randomNumber());

        // when
        MvcResult result = mockMvc.perform(post("/api/cart/increment")
                .content(objectMapper.writeValueAsString(incrementCartItemRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        // then
        then(result.getResponse().getContentAsString()).contains("must not be null");

    }

    @Test
    void it_should_not_increment_cart_item_when_cart_item_id_is_invalid() throws Exception {

        // given
        IncrementCartItemRequest incrementCartItemRequest = new IncrementCartItemRequest();
        incrementCartItemRequest.setAmount(faker.number().randomDigitNotZero());
        incrementCartItemRequest.setCartItemId(null);

        // when
        MvcResult result = mockMvc.perform(post("/api/cart/increment")
                .content(objectMapper.writeValueAsString(incrementCartItemRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        // then
        then(result.getResponse().getContentAsString()).contains("must not be null");

    }

    @Test
    void it_should_decrement_cart_item() throws Exception {

        // given
        DecrementCartItemRequest decrementCartItemRequest = new DecrementCartItemRequest();
        decrementCartItemRequest.setAmount(faker.number().randomDigitNotZero());
        decrementCartItemRequest.setCartItemId(faker.number().randomNumber());

        CartResponse cartResponseExpected = new CartResponse();

        given(cartService.decrementCartItem(decrementCartItemRequest.getCartItemId(), decrementCartItemRequest.getAmount())).willReturn(cartResponseExpected);

        // when
        MvcResult result = mockMvc.perform(post("/api/cart/decrement")
                .content(objectMapper.writeValueAsString(decrementCartItemRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // then
        verify(cartService, times(1)).decrementCartItem(decrementCartItemRequest.getCartItemId(), decrementCartItemRequest.getAmount());
        then(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(cartResponseExpected));
    }

    @Test
    void it_should_not_decrement_cart_item_when_amount_is_invalid() throws Exception {

        // given
        DecrementCartItemRequest decrementCartItemRequest = new DecrementCartItemRequest();
        decrementCartItemRequest.setAmount(null);
        decrementCartItemRequest.setCartItemId(faker.number().randomNumber());

        // when
        MvcResult result = mockMvc.perform(post("/api/cart/decrement")
                .content(objectMapper.writeValueAsString(decrementCartItemRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        // then
        then(result.getResponse().getContentAsString()).contains("must not be null");

    }

    @Test
    void it_should_not_decrement_cart_item_when_cart_item_id_is_invalid() throws Exception {

        // given
        DecrementCartItemRequest decrementCartItemRequest = new DecrementCartItemRequest();
        decrementCartItemRequest.setAmount(faker.number().randomDigitNotZero());
        decrementCartItemRequest.setCartItemId(null);

        // when
        MvcResult result = mockMvc.perform(post("/api/cart/decrement")
                .content(objectMapper.writeValueAsString(decrementCartItemRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        // then
        then(result.getResponse().getContentAsString()).contains("must not be null");

    }

    @Test
    void it_should_fetch_cart() throws Exception {

        // given
        CartResponse cartResponseExpected = new CartResponse();

        given(cartService.fetchCart()).willReturn(cartResponseExpected);

        // when
        MvcResult result = mockMvc.perform(get("/api/cart")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // then
        verify(cartService, times(1)).fetchCart();
        then(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(cartResponseExpected));
    }

    @Test
    void it_should_remove_from_cart() throws Exception {

        // given
        RemoveFromCartRequest removeFromCartRequest = new RemoveFromCartRequest();
        removeFromCartRequest.setCartItemId(faker.number().randomNumber());

        CartResponse cartResponseExpected = new CartResponse();

        given(cartService.removeFromCart(removeFromCartRequest.getCartItemId())).willReturn(cartResponseExpected);

        // when
        MvcResult result = mockMvc.perform(post("/api/cart/remove")
                .content(objectMapper.writeValueAsString(removeFromCartRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        //then
        verify(cartService, times(1)).removeFromCart(removeFromCartRequest.getCartItemId());
        then(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(cartResponseExpected));
    }

    @Test
    void it_should_not_remove_from_cart_when_cart_item_id_is_invalid() throws Exception {

        // given
        RemoveFromCartRequest removeFromCartRequest = new RemoveFromCartRequest();
        removeFromCartRequest.setCartItemId(null);

        // when
        MvcResult result = mockMvc.perform(post("/api/cart/remove")
                .content(objectMapper.writeValueAsString(removeFromCartRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        // then
        then(result.getResponse().getContentAsString()).contains("must not be null");

    }

    @Test
    void it_should_empty_cart() throws Exception {

        // when
        mockMvc.perform(delete("/api/cart")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // then
        verify(cartService, times(1)).emptyCart();
    }

    @Test
    void it_should_confirm_cart() throws Exception {

        // given
        ConfirmCartRequest confirmCartRequest = new ConfirmCartRequest();

        confirmCartRequest.setCartItems(new ArrayList<>());
        confirmCartRequest.setDiscount(DiscountDTO.builder().build());
        confirmCartRequest.setTotalCartPrice((float) faker.number().randomNumber());
        confirmCartRequest.setTotalCargoPrice((float) faker.number().randomNumber());
        confirmCartRequest.setTotalPrice((float) faker.number().randomNumber());

        given(cartService.confirmCart(confirmCartRequest)).willReturn(true);

        // when
        MvcResult result = mockMvc.perform(post("/api/cart/confirm")
                .content(objectMapper.writeValueAsString(confirmCartRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // then
        verify(cartService, times(1)).confirmCart(confirmCartRequest);
        then(result.getResponse().getContentAsString()).isEqualTo("true");
    }

    @Test
    void it_should_not_confirm_cart_if_invalid_field() throws Exception {

        // when, then
        mockMvc.perform(post("/api/cart/confirm")
                .content(objectMapper.writeValueAsString(new ConfirmCartRequest()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

    }

}