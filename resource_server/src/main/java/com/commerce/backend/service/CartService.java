package com.commerce.backend.service;

import com.commerce.backend.model.Cart;

import java.security.Principal;

public interface CartService {
    Cart addToCart(Principal principal, Long id, Integer amount);

    Cart fetchCart(Principal principal);

    Cart removeFromCart(Principal principal, Long id);

    Boolean confirmCart(Principal principal, Cart cart);

    void emptyCart(Principal principal);

}
