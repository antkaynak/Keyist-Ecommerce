package com.commerce.backend.secured;

import com.commerce.backend.model.Cart;
import com.commerce.backend.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@RestController
public class DiscountController extends SecuredApiController {

    private final DiscountService discountService;

    @Autowired
    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @RequestMapping(value = "/cart/discount", method = RequestMethod.GET, params = "code")
    public ResponseEntity applyDiscount(@RequestParam("code") String code, Principal principal) {
        Cart cart = discountService.applyDiscount(principal, code);
        return new ResponseEntity<Cart>(cart, HttpStatus.OK);
    }

}
