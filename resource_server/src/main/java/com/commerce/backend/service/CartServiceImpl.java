package com.commerce.backend.service;

import com.commerce.backend.dao.CartItemRepository;
import com.commerce.backend.dao.CartRepository;
import com.commerce.backend.dao.ProductDisplayRepository;
import com.commerce.backend.dao.UserRepository;
import com.commerce.backend.model.Cart;
import com.commerce.backend.model.CartItem;
import com.commerce.backend.model.ProductDisplay;
import com.commerce.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {


    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductDisplayRepository productDisplayRepository;
    private final PriceService priceService;
    private final UserRepository userRepository;


    @Autowired
    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository,
                           ProductDisplayRepository productDisplayRepository, PriceService priceService,
                           UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productDisplayRepository = productDisplayRepository;
        this.priceService = priceService;
        this.userRepository = userRepository;
    }

    @Override
    public Cart addToCart(Principal principal, Long id, Integer amount) {
        User user = getUserFromPrinciple(principal);
        if (amount <= 0 || id <= 0) {
            throw new IllegalArgumentException("Invalid parameters");
        }

        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setCartUser(user);
            //usersRepository.save(user); deleted because it caused the cart to be saved twice
        } else if (cart.getCartItemList() != null || !cart.getCartItemList().isEmpty()) {
            for (CartItem i : cart.getCartItemList()) {
                if (i.getCartProduct().getId().equals(id)) {
                    i.setAmount(i.getAmount() + amount);
//                CartItem ct = cartItemRepository.save(i);
                    Cart returnCart = priceService.calculatePrice(cart);
                    cartRepository.save(returnCart);
//                    return new ResponseEntity<Cart>(returnCart,HttpStatus.OK);
                    return returnCart;
                }
            }
        }

        Optional optional = productDisplayRepository.findById(id);
        if (!optional.isPresent()) {
            throw new IllegalArgumentException("Product not found.");
        }

        ProductDisplay product = (ProductDisplay) optional.get();
        CartItem cartItem = new CartItem();
        cartItem.setAmount(amount);
        cartItem.setCartProduct(product);

        //this will save the cart object as well because there is cascading from cartItem
        cartItem.setCart(cart);
        if (cart.getCartItemList() == null) {
            cart.setCartItemList(new ArrayList<>());
        }
        cart.getCartItemList().add(cartItem);
        cart = priceService.calculatePrice(cart);
        cartItemRepository.save(cartItem);

        return cart;
    }

    @Override
    public Cart fetchCart(Principal principal) {
        System.out.println("FETCH CART");
        User user = getUserFromPrinciple(principal);
        return user.getCart();
    }

    @Override
    public Cart removeFromCart(Principal principal, Long id) {
        System.out.println("Remove CartItem id " + id);
        User user = getUserFromPrinciple(principal);
        Cart cart = user.getCart();
        if (cart == null) {
            throw new IllegalArgumentException("Cart not found");
        }
        List<CartItem> cartItemsList = cart.getCartItemList();
        CartItem cartItemToDelete = null;
        for (CartItem i : cartItemsList) {
            if (i.getId().equals(id)) {
                cartItemToDelete = i;
            }
        }
        if (cartItemToDelete == null) {
            throw new IllegalArgumentException("CartItem not found");
        }

        cartItemsList.remove(cartItemToDelete);

        if (cart.getCartItemList() == null || cart.getCartItemList().size() == 0) {
            //TODO make it so it can be deleted with online cartRepository.delete method
//            cartRepository.delete(cart);
            user.setCart(null);
            //setting it to null will delete it
            //because of the orphanRemove mark on the field
            userRepository.save(user);
            return null;
        }

        cart.setCartItemList(cartItemsList);
        cart = priceService.calculatePrice(cart);
        cartItemRepository.delete(cartItemToDelete);

        return cart;
    }

    @Override
    public Boolean confirmCart(Principal principal, Cart cart) {
        User user = getUserFromPrinciple(principal);
        Cart dbCart = user.getCart();
        if (dbCart == null) {
            throw new IllegalArgumentException("Cart not found");
        }
        List<CartItem> dbCartItemsList = dbCart.getCartItemList();
        List<CartItem> cartItemsList = cart.getCartItemList();
        if (dbCartItemsList.size() != cartItemsList.size()) {
            return false;
        }

        for (int i = 0; i < dbCartItemsList.size(); i++) {
            if (!dbCartItemsList.get(i).getId().equals(cartItemsList.get(i).getId()) &&
                    !dbCartItemsList.get(i).getAmount().equals(cartItemsList.get(i).getAmount()) &&
                    !dbCartItemsList.get(i).getCartProduct().getId().equals(cartItemsList.get(i).getCartProduct().getId())) {
                return false;
            }
        }
        if (
                dbCart.getTotalPrice().equals(cart.getTotalPrice())
                        && dbCart.getTotalCargoPrice().equals(cart.getTotalCargoPrice())
                        && dbCart.getId().equals(cart.getId())) {
            if (dbCart.getCartDiscount() != null && cart.getCartDiscount() != null) {
                if (dbCart.getCartDiscount().getDiscountPercent().equals(cart.getCartDiscount().getDiscountPercent())
                        && dbCart.getCartDiscount().getCode().equals(cart.getCartDiscount().getCode())) {
                    System.out.println("equals");
                    return true;
                }
            } else if (dbCart.getCartDiscount() == null && cart.getCartDiscount() == null) {
                System.out.println("equals");
                return true;
            }

        }
        System.out.println("no u");
        System.out.println(dbCart.getCartItemList().equals(cart.getCartItemList()));
        return false;
    }

    @Override
    public void emptyCart(Principal principal) {
        User user = getUserFromPrinciple(principal);
        user.setCart(null);
        userRepository.save(user);
    }


    private User getUserFromPrinciple(Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new IllegalArgumentException("Invalid access");
        }
        User user = userRepository.findByEmail(principal.getName());
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return user;
    }


}
