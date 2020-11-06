package com.commerce.backend.api;


import com.commerce.backend.model.request.order.PostOrderRequest;
import com.commerce.backend.model.response.order.OrderResponse;
import com.commerce.backend.service.OrderService;
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
import org.springframework.util.LinkedMultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
@WebMvcTest(OrderController.class)
@AutoConfigureWebClient
@WithMockUser
@ComponentScan(basePackages = {"com.commerce.backend.constants"})
class OrderControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private OrderService orderService;
    @Autowired
    private MockMvc mockMvc;
    private Faker faker;

    @BeforeEach
    public void setUp() {
        faker = new Faker();
    }

    @Test
    void it_should_get_all_orders_count() throws Exception {

        // given
        Integer orderCount = (int) faker.number().randomNumber();

        given(orderService.getAllOrdersCount()).willReturn(orderCount);


        // when
        MvcResult result = mockMvc.perform(get("/api/order/count")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();


        // then
        verify(orderService, times(1)).getAllOrdersCount();
        then(result.getResponse().getContentAsString()).isEqualTo(orderCount.toString());
    }

    @Test
    void it_should_get_all_orders() throws Exception {

        // given
        Integer page = (int) faker.number().randomNumber();
        Integer pageSize = (int) faker.number().randomNumber();

        List<OrderResponse> orderResponseList = new ArrayList<>();
        OrderResponse orderResponse = new OrderResponse();
        orderResponseList.add(orderResponse);

        given(orderService.getAllOrders(page, pageSize)).willReturn(orderResponseList);

        // when
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("page", page.toString());
        requestParams.add("size", pageSize.toString());
        MvcResult result = mockMvc.perform(get("/api/order")
                .params(requestParams)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // then
        verify(orderService, times(1)).getAllOrders(page, pageSize);
        then(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(orderResponseList));
    }

    @Test
    void it_should_not_get_all_orders_if_missing_params() throws Exception {

        // given
        Integer page = (int) faker.number().randomNumber();
        Integer pageSize = (int) faker.number().randomNumber();

        // when
        MvcResult result = mockMvc.perform(get("/api/order")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andReturn();

        // then
        then(result.getResponse().getContentAsString()).contains("is not present");
    }


    @Test
    void it_should_not_get_all_orders_if_page_is_invalid() throws Exception {

        // given
        Integer page = (int) faker.number().randomNumber() * -1;
        Integer pageSize = (int) faker.number().randomNumber();

        // when
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("page", page.toString());
        requestParams.add("size", pageSize.toString());
        MvcResult result = mockMvc.perform(get("/api/order")
                .params(requestParams)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        // then
        then(result.getResponse().getContentAsString()).contains("Invalid page");
    }

    @Test
    void it_should_not_get_all_orders_if_size_is_invalid() throws Exception {

        // given
        Integer page = (int) faker.number().randomNumber();
        Integer pageSize = (int) faker.number().randomNumber() * -1;

        // when
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("page", page.toString());
        requestParams.add("size", pageSize.toString());
        MvcResult result = mockMvc.perform(get("/api/order")
                .params(requestParams)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        // then
        then(result.getResponse().getContentAsString()).contains("Invalid pageSize");
    }

    @Test
    void it_should_post_order() throws Exception {

        // given
        PostOrderRequest postOrderRequest = new PostOrderRequest();
        postOrderRequest.setShipName(faker.address().firstName());
        postOrderRequest.setShipAddress(faker.address().streetAddress());
        postOrderRequest.setBillingAddress(faker.address().streetAddress());
        postOrderRequest.setCity(faker.address().city());
        postOrderRequest.setState(faker.address().state());
        postOrderRequest.setZip(faker.number().digits(6));
        postOrderRequest.setCountry(faker.address().country());
        postOrderRequest.setPhone(faker.number().digits(12));

        OrderResponse orderResponse = new OrderResponse();

        given(orderService.postOrder(postOrderRequest)).willReturn(orderResponse);

        // when
        MvcResult result = mockMvc.perform(post("/api/order")
                .content(objectMapper.writeValueAsString(postOrderRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // then
        verify(orderService, times(1)).postOrder(postOrderRequest);
        then(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(orderResponse));
    }

    @Test
    void it_should_not_post_order_if_invalid_request_body() throws Exception {

        // given
        PostOrderRequest postOrderRequest = new PostOrderRequest();
        postOrderRequest.setShipName("+");

        // when
        MvcResult result = mockMvc.perform(post("/api/order")
                .content(objectMapper.writeValueAsString(postOrderRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        // then
        then(result.getResponse().getContentAsString()).contains("must not be blank");
        then(result.getResponse().getContentAsString()).contains("must match");
    }


}