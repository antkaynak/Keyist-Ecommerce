package com.commerce.backend.service;

import com.commerce.backend.model.Cart;
import com.commerce.backend.model.CartItem;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

@Service
public class PriceService {

    public Cart calculatePrice(Cart cart) {

        Float totalPrice = 0F;
        Float totalCargoPrice = 0F;

        for (CartItem i : cart.getCartItemList()) {
            System.out.println("amount " + i.getAmount());
            totalPrice = totalPrice + (((i.getCartProduct().getPrice() + i.getCartProduct().getCargoPrice()) * i.getAmount()));
            totalCargoPrice = totalCargoPrice + (i.getCartProduct().getCargoPrice() * i.getAmount());
        }

        //Applying discount percent if exists
        if (cart.getCartDiscount() != null) {
            totalPrice = totalPrice - ((totalPrice * cart.getCartDiscount().getDiscountPercent()) / 100);
        }

        cart.setTotalPrice(roundTwoDecimals(totalPrice));
        cart.setTotalCargoPrice(roundTwoDecimals(totalCargoPrice));
//        System.out.println(cart); throws stackoverflow
        return cart;
    }

    private float roundTwoDecimals(float d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Float.valueOf(twoDForm.format(d));
    }

}
