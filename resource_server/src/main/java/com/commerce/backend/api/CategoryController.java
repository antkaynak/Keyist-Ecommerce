package com.commerce.backend.api;

import com.commerce.backend.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController extends ApiController {

    private final ProductCategoryService productCategoryService;

    @Autowired
    public CategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public ResponseEntity getAllCategories() {
        List returnList = productCategoryService.findAllByOrderByName();
        return new ResponseEntity<>(returnList, HttpStatus.OK);
    }

}
