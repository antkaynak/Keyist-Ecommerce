package com.commerce.backend.secured;


import com.commerce.backend.model.Cart;
import com.commerce.backend.service.CartService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;


@RestController
public class CartController extends SecuredApiController {


    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


    @RequestMapping(value = "/cart", method = RequestMethod.POST)
    public ResponseEntity addToCart(@RequestBody String payload, Principal principal) throws IOException {
        System.out.println("Add to cart");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(payload);
        JsonNode jsonNode1 = actualObj.get("productId");
        JsonNode jsonNode2 = actualObj.get("amount");
        Long requestProductId = Long.parseLong(jsonNode1.textValue());
        Integer requestProductAmount = Integer.parseInt(jsonNode2.textValue());
        Cart cart = cartService.addToCart(principal, requestProductId, requestProductAmount);
        return new ResponseEntity<Cart>(cart, HttpStatus.OK);
    }

    @RequestMapping(value = "/cart", method = RequestMethod.GET)
    public ResponseEntity fetchCart(Principal principal) {
        Cart cart = cartService.fetchCart(principal);
        return new ResponseEntity<Cart>(cart, HttpStatus.OK);
    }

    @RequestMapping(value = "/cart", method = RequestMethod.DELETE, params = "id")
    public ResponseEntity removeFromCart(@RequestParam("id") Long id, Principal principal) {
        Cart cart = cartService.removeFromCart(principal, id);
        return new ResponseEntity<Cart>(cart, HttpStatus.OK);
    }

    @RequestMapping(value = "/cart/confirm", method = RequestMethod.POST)
    public ResponseEntity confirmCart(@Valid @RequestBody Cart cart, BindingResult bindingResult, Principal principal) {
        System.out.println("RequestBody -> " + cart.toString());
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Boolean result = cartService.confirmCart(principal, cart);
        return result ? new ResponseEntity(HttpStatus.OK) : new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/cart", method = RequestMethod.DELETE)
    public ResponseEntity emptyCart(Principal principal) {
        cartService.emptyCart(principal);
        return new ResponseEntity(HttpStatus.OK);
    }

}
