package com.commerce.backend.service;

import com.commerce.backend.model.entity.Cart;
import com.commerce.backend.model.request.cart.ConfirmCartRequest;
import com.commerce.backend.model.response.cart.CartResponse;


public interface CartService {
    CartResponse addToCart(Long id, Integer amount);

    CartResponse incrementCartItem(Long cartItemId, Integer amount);

    CartResponse decrementCartItem(Long cartItemId, Integer amount);

    CartResponse fetchCart();

    CartResponse removeFromCart(Long id);

    boolean confirmCart(ConfirmCartRequest confirmCartRequest);

    Cart getCart();

    void saveCart(Cart cart);

    void emptyCart();

    Cart calculatePrice(Cart cart);

}
