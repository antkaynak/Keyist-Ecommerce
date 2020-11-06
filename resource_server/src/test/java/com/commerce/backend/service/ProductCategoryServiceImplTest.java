package com.commerce.backend.service;

import com.commerce.backend.converter.category.ProductCategoryResponseConverter;
import com.commerce.backend.error.exception.ResourceNotFoundException;
import com.commerce.backend.model.dto.CategoryDTO;
import com.commerce.backend.model.entity.ProductCategory;
import com.commerce.backend.model.response.category.ProductCategoryResponse;
import com.commerce.backend.service.cache.ProductCategoryCacheService;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class ProductCategoryServiceImplTest {

    @InjectMocks
    private ProductCategoryServiceImpl productCategoryService;

    @Mock
    private ProductCategoryCacheService productCategoryCacheService;

    @Mock
    private ProductCategoryResponseConverter productCategoryResponseConverter;

    private Faker faker;

    @BeforeEach
    public void setUp() {
        faker = new Faker();
    }

    @Test
    void it_should_find_all_categories() {

        // given
        String categoryName = faker.lorem().word();
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName(categoryName);
        ProductCategoryResponse productCategoryResponse = new ProductCategoryResponse();
        productCategoryResponse.setCategory(CategoryDTO.builder().name(categoryName).build());

        List<ProductCategory> productCategoryList = Stream.generate(() -> productCategory)
                .limit(faker.number().randomDigitNotZero())
                .collect(Collectors.toList());

        given(productCategoryCacheService.findAllByOrderByName()).willReturn(productCategoryList);
        given(productCategoryResponseConverter.apply(any(ProductCategory.class))).willReturn(productCategoryResponse);

        // when
        List<ProductCategoryResponse> productCategoryResponseList = productCategoryService.findAllByOrderByName();

        // then
        then(productCategoryResponseList.size()).isEqualTo(productCategoryList.size());
        productCategoryResponseList.forEach(productCategoryResponse1 -> then(productCategoryResponse1.getCategory().getName()).isEqualTo(productCategory.getName()));

    }

    @Test
    void it_should_throw_exception_when_no_category() {

        // given
        given(productCategoryCacheService.findAllByOrderByName()).willReturn(Collections.emptyList());

        // when, then
        assertThatThrownBy(() -> productCategoryService.findAllByOrderByName())
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Could not find product categories");

    }

}