package com.commerce.backend.api;

import com.commerce.backend.model.response.color.ProductColorResponse;
import com.commerce.backend.service.ProductColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ColorController extends PublicApiController {

    private final ProductColorService productColorService;

    @Autowired
    public ColorController(ProductColorService productColorService) {
        this.productColorService = productColorService;
    }


    @GetMapping(value = "/colors")
    public ResponseEntity<List<ProductColorResponse>> getAllColors() {
        List<ProductColorResponse> productColors = productColorService.findAll();
        return new ResponseEntity<>(productColors, HttpStatus.OK);
    }
}
