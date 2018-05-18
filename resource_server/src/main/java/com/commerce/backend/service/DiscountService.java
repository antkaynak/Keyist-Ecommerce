package com.commerce.backend.service;

import com.commerce.backend.model.Cart;

import java.security.Principal;

public interface DiscountService {
    Cart applyDiscount(Principal principal, String code);
}
