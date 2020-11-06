package com.commerce.backend.api;


import com.commerce.backend.model.response.product.ProductDetailsResponse;
import com.commerce.backend.model.response.product.ProductResponse;
import com.commerce.backend.model.response.product.ProductVariantResponse;
import com.commerce.backend.service.ProductService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
@WebMvcTest(ProductController.class)
@AutoConfigureWebClient
@ComponentScan(basePackages = {"com.commerce.backend.constants"})
class ProductControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private ProductService productService;
    @Autowired
    private MockMvc mockMvc;
    private Faker faker;

    @BeforeEach
    public void setUp() {
        faker = new Faker();
    }

    @Test
    void it_should_get_all_product_variants() throws Exception {

        // given
        Integer page = (int) faker.number().randomNumber();
        Integer size = (int) faker.number().randomNumber();
        String sort = faker.lorem().word();
        String category = faker.lorem().word();
        Float minPrice = (float) faker.number().randomNumber();
        Float maxPrice = (float) faker.number().randomNumber();
        String color = faker.lorem().word();


        List<ProductVariantResponse> productVariantResponses = Stream.generate(ProductVariantResponse::new)
                .limit(faker.number().randomDigitNotZero())
                .collect(Collectors.toList());

        given(productService.getAll(page, size, sort, category, minPrice, maxPrice, color)).willReturn(productVariantResponses);

        // when
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("page", page.toString());
        requestParams.add("size", size.toString());
        requestParams.add("sort", sort);
        requestParams.add("category", category);
        requestParams.add("minPrice", minPrice.toString());
        requestParams.add("maxPrice", maxPrice.toString());
        requestParams.add("color", color);
        MvcResult result = mockMvc.perform(get("/api/public/product")
                .params(requestParams)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();


        // then
        verify(productService, times(1)).getAll(page, size, sort, category, minPrice, maxPrice, color);
        then(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(productVariantResponses));
    }

    @Test
    void it_should_not_get_all_product_variants_if_invalid_page_param() throws Exception {

        // given
        Integer page = (int) faker.number().randomNumber() * -1;
        Integer size = (int) faker.number().randomNumber();


        // when
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("page", page.toString());
        requestParams.add("size", size.toString());
        MvcResult result = mockMvc.perform(get("/api/public/product")
                .params(requestParams)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();


        // then
        then(result.getResponse().getContentAsString()).contains("Invalid page");
    }

    @Test
    void it_should_not_get_all_product_variants_if_invalid_size_param() throws Exception {

        // given
        Integer page = (int) faker.number().randomNumber();
        Integer size = (int) faker.number().randomNumber() * -1;


        // when
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("page", page.toString());
        requestParams.add("size", size.toString());
        MvcResult result = mockMvc.perform(get("/api/public/product")
                .params(requestParams)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();


        // then
        then(result.getResponse().getContentAsString()).contains("Invalid pageSize");
    }

    @Test
    void it_should_get_all_product_count() throws Exception {

        // given
        String category = faker.lorem().word();
        Float minPrice = (float) faker.number().randomNumber();
        Float maxPrice = (float) faker.number().randomNumber();
        String color = faker.lorem().word();


        Long count = faker.number().randomNumber();

        given(productService.getAllCount(category, minPrice, maxPrice, color)).willReturn(count);

        // when
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("category", category);
        requestParams.add("minPrice", minPrice.toString());
        requestParams.add("maxPrice", maxPrice.toString());
        requestParams.add("color", color);
        MvcResult result = mockMvc.perform(get("/api/public/product/count")
                .params(requestParams)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();


        // then
        verify(productService, times(1)).getAllCount(category, minPrice, maxPrice, color);
        then(result.getResponse().getContentAsString()).isEqualTo(count.toString());
    }

    @Test
    void it_should_get_product_by_url() throws Exception {

        // given
        String productUrl = faker.lorem().word();

        ProductDetailsResponse productDetailsResponse = new ProductDetailsResponse();

        given(productService.findByUrl(productUrl)).willReturn(productDetailsResponse);

        // when
        MvcResult result = mockMvc.perform(get(String.format("/api/public/product/%s", productUrl))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();


        // then
        verify(productService, times(1)).findByUrl(productUrl);
        then(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(productDetailsResponse));
    }

    @Test
    void it_should_not_get_product_by_url_if_slug_is_blank() throws Exception {

        // when
        MvcResult result = mockMvc.perform(get("/api/public/product/ ")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();


        // then
        then(result.getResponse().getContentAsString()).contains("Invalid url params");
    }

    @Test
    void it_should_get_related_product_by_url() throws Exception {

        // given
        String productUrl = faker.lorem().word();

        List<ProductResponse> productResponseList = Stream.generate(ProductResponse::new)
                .limit(faker.number().randomDigitNotZero())
                .collect(Collectors.toList());

        given(productService.getRelatedProducts(productUrl)).willReturn(productResponseList);

        // when
        MvcResult result = mockMvc.perform(get(String.format("/api/public/product/related/%s", productUrl))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();


        // then
        verify(productService, times(1)).getRelatedProducts(productUrl);
        then(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(productResponseList));
    }

    @Test
    void it_should_not_get_related_product_by_url_if_slug_is_blank() throws Exception {

        // when
        MvcResult result = mockMvc.perform(get("/api/public/product/related/ ")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();


        // then
        then(result.getResponse().getContentAsString()).contains("Invalid url params");
    }

    @Test
    void it_should_get_newly_added_product() throws Exception {

        // given
        List<ProductResponse> productResponseList = Stream.generate(ProductResponse::new)
                .limit(faker.number().randomDigitNotZero())
                .collect(Collectors.toList());

        given(productService.getNewlyAddedProducts()).willReturn(productResponseList);

        // when
        MvcResult result = mockMvc.perform(get("/api/public/product/recent")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();


        // then
        verify(productService, times(1)).getNewlyAddedProducts();
        then(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(productResponseList));
    }

    @Test
    void it_should_get_most_selling_product() throws Exception {

        // given
        List<ProductVariantResponse> productVariantResponseList = Stream.generate(ProductVariantResponse::new)
                .limit(faker.number().randomDigitNotZero())
                .collect(Collectors.toList());

        given(productService.getMostSelling()).willReturn(productVariantResponseList);

        // when
        MvcResult result = mockMvc.perform(get("/api/public/product/mostselling")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();


        // then
        verify(productService, times(1)).getMostSelling();
        then(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(productVariantResponseList));
    }

    @Test
    void it_should_get_interested_product() throws Exception {

        // given
        List<ProductResponse> productResponseList = Stream.generate(ProductResponse::new)
                .limit(faker.number().randomDigitNotZero())
                .collect(Collectors.toList());

        given(productService.getInterested()).willReturn(productResponseList);

        // when
        MvcResult result = mockMvc.perform(get("/api/public/product/interested")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();


        // then
        verify(productService, times(1)).getInterested();
        then(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(productResponseList));
    }

    @Test
    void it_should_get_searched_product() throws Exception {

        // given
        Integer page = (int) faker.number().randomNumber();
        Integer size = (int) faker.number().randomNumber();
        String keyword = faker.lorem().word();

        List<ProductResponse> productResponseList = Stream.generate(ProductResponse::new)
                .limit(faker.number().randomDigitNotZero())
                .collect(Collectors.toList());

        given(productService.searchProductDisplay(keyword, page, size)).willReturn(productResponseList);

        // when
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("page", page.toString());
        requestParams.add("size", size.toString());
        requestParams.add("keyword", keyword);
        MvcResult result = mockMvc.perform(get("/api/public/product/search")
                .params(requestParams)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();


        // then
        verify(productService, times(1)).searchProductDisplay(keyword, page, size);
        then(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(productResponseList));
    }

}