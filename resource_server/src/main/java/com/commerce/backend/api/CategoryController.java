package com.commerce.backend.api;

import com.commerce.backend.model.response.category.ProductCategoryResponse;
import com.commerce.backend.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController extends PublicApiController {

    private final ProductCategoryService productCategoryService;

    @Autowired
    public CategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @GetMapping(value = "/category")
    public ResponseEntity<List<ProductCategoryResponse>> getAllCategories() {
        List<ProductCategoryResponse> productCategories = productCategoryService.findAllByOrderByName();
        return new ResponseEntity<>(productCategories, HttpStatus.OK);
    }

}
