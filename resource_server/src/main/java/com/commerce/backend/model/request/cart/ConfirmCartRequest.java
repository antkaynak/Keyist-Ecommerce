package com.commerce.backend.model.request.cart;

import com.commerce.backend.model.dto.CartItemDTO;
import com.commerce.backend.model.dto.DiscountDTO;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ConfirmCartRequest {

    @NotNull
    private List<CartItemDTO> cartItems;

    private DiscountDTO discount;

    @Min(0)
    private Float totalCartPrice;

    @Min(0)
    private Float totalCargoPrice;

    @Min(0)
    private Float totalPrice;


}
