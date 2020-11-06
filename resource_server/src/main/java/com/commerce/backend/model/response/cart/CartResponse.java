package com.commerce.backend.model.response.cart;

import com.commerce.backend.model.dto.CartItemDTO;
import com.commerce.backend.model.dto.DiscountDTO;
import lombok.Data;

import java.util.List;

@Data
public class CartResponse {
    private List<CartItemDTO> cartItems;
    private DiscountDTO discount;
    private Float totalCartPrice;
    private Float totalCargoPrice;
    private Float totalPrice;
}
