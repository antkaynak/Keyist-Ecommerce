package com.commerce.backend.api;

import com.commerce.backend.model.request.discount.ApplyDiscountRequest;
import com.commerce.backend.service.DiscountService;
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

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
@WebMvcTest(DiscountController.class)
@AutoConfigureWebClient
@WithMockUser
@ComponentScan(basePackages = {"com.commerce.backend.constants"})
class DiscountControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private DiscountService discountService;
    @Autowired
    private MockMvc mockMvc;
    private Faker faker;

    @BeforeEach
    public void setUp() {
        faker = new Faker();
    }

    @Test
    void it_should_apply_discount() throws Exception {

        // given
        String discountCode = faker.lorem().word();

        ApplyDiscountRequest applyDiscountRequest = new ApplyDiscountRequest();
        applyDiscountRequest.setCode(discountCode);

        // when
        mockMvc.perform(post("/api/cart/discount")
                .content(objectMapper.writeValueAsString(applyDiscountRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // then
        verify(discountService, times(1)).applyDiscount(discountCode);
    }

    @Test
    void it_should_not_apply_discount_if_code_is_invalid() throws Exception {

        // given
        String discountCode = "";

        ApplyDiscountRequest applyDiscountRequest = new ApplyDiscountRequest();
        applyDiscountRequest.setCode(discountCode);

        // when
        MvcResult result = mockMvc.perform(post("/api/cart/discount")
                .content(objectMapper.writeValueAsString(applyDiscountRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        // then
        then(result.getResponse().getContentAsString()).contains("must not be blank");


    }

}