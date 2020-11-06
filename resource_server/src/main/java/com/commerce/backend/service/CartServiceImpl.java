package com.commerce.backend.service;

import com.commerce.backend.converter.cart.CartResponseConverter;
import com.commerce.backend.dao.CartRepository;
import com.commerce.backend.error.exception.InvalidArgumentException;
import com.commerce.backend.error.exception.ResourceNotFoundException;
import com.commerce.backend.model.dto.CartItemDTO;
import com.commerce.backend.model.entity.Cart;
import com.commerce.backend.model.entity.CartItem;
import com.commerce.backend.model.entity.ProductVariant;
import com.commerce.backend.model.entity.User;
import com.commerce.backend.model.request.cart.ConfirmCartRequest;
import com.commerce.backend.model.response.cart.CartResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;
    private final UserService userService;
    private final CartResponseConverter cartResponseConverter;


    @Autowired
    public CartServiceImpl(CartRepository cartRepository,
                           ProductService productService,
                           UserService userService,
                           CartResponseConverter cartResponseConverter) {
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.userService = userService;
        this.cartResponseConverter = cartResponseConverter;
    }

    @Override
    public CartResponse addToCart(Long productVariantId, Integer amount) {
        User user = userService.getUser();
        Cart cart = user.getCart();

        if (Objects.nonNull(cart) && Objects.nonNull(cart.getCartItemList()) && !cart.getCartItemList().isEmpty()) {
            Optional<CartItem> cartItem = cart.getCartItemList()
                    .stream()
                    .filter(ci -> ci.getProductVariant().getId().equals(productVariantId)).findFirst();
            if (cartItem.isPresent()) {
                if (cartItem.get().getProductVariant().getStock() < (cartItem.get().getAmount() + amount)) {
                    throw new InvalidArgumentException("Product does not have desired stock.");
                }
                cartItem.get().setAmount(cartItem.get().getAmount() + amount);
                Cart updatedCart = calculatePrice(cart);
                cart = cartRepository.save(updatedCart);
                return cartResponseConverter.apply(cart);
            }
        }

        if (Objects.isNull(cart)) {
            cart = createCart(user);
        }

        ProductVariant productVariant = productService.findProductVariantById(productVariantId);

        if (productVariant.getStock() < amount) {
            throw new InvalidArgumentException("Product does not have desired stock.");
        }

        CartItem cartItem = new CartItem();
        cartItem.setAmount(amount);
        cartItem.setProductVariant(productVariant);
        cartItem.setCart(cart);

        if (Objects.isNull(cart.getCartItemList())) {
            cart.setCartItemList(new ArrayList<>());
        }
        cart.getCartItemList().add(cartItem);
        cart = calculatePrice(cart);

        cart = cartRepository.save(cart);
        return cartResponseConverter.apply(cart);
    }

    @Override
    public CartResponse incrementCartItem(Long cartItemId, Integer amount) {
        User user = userService.getUser();
        Cart cart = user.getCart();
        if (Objects.isNull(cart) || Objects.isNull(cart.getCartItemList()) || cart.getCartItemList().isEmpty()) {
            throw new ResourceNotFoundException("Empty cart");
        }

        CartItem cartItem = cart.getCartItemList()
                .stream()
                .filter(ci -> ci.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("CartItem not found"));

        if (cartItem.getProductVariant().getStock() < (cartItem.getAmount() + amount)) {
            throw new InvalidArgumentException("Product does not have desired stock.");
        }

        cartItem.setAmount(cartItem.getAmount() + amount);
        cart = calculatePrice(cart);
        cart = cartRepository.save(cart);
        return cartResponseConverter.apply(cart);
    }

    @Override
    public CartResponse decrementCartItem(Long cartItemId, Integer amount) {
        User user = userService.getUser();
        Cart cart = user.getCart();
        if (Objects.isNull(cart) || Objects.isNull(cart.getCartItemList()) || cart.getCartItemList().isEmpty()) {
            throw new ResourceNotFoundException("Empty cart");
        }

        CartItem cartItem = cart.getCartItemList()
                .stream()
                .filter(ci -> ci.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("CartItem not found"));


        if (cartItem.getAmount() <= amount) {
            List<CartItem> cartItemList = cart.getCartItemList();
            cartItemList.remove(cartItem);
            if (Objects.isNull(cart.getCartItemList()) || cart.getCartItemList().isEmpty()) {
                user.setCart(null);
                userService.saveUser(user);
                return null;
            }
            cart.setCartItemList(cartItemList);
            cart = calculatePrice(cart);
            cart = cartRepository.save(cart);
            return cartResponseConverter.apply(cart);
        }

        cartItem.setAmount(cartItem.getAmount() - amount);
        cart = calculatePrice(cart);
        cart = cartRepository.save(cart);
        return cartResponseConverter.apply(cart);
    }

    @Override
    public CartResponse fetchCart() {
        Cart cart = userService.getUser().getCart();
        if (cart == null) {
            return null;
        }
        return cartResponseConverter.apply(cart);
    }

    @Override
    public CartResponse removeFromCart(Long cartItemId) {
        User user = userService.getUser();
        Cart cart = user.getCart();

        if (Objects.isNull(cart) || Objects.isNull(cart.getCartItemList()) || cart.getCartItemList().isEmpty()) {
            throw new ResourceNotFoundException("Cart or CartItem not found");
        }

        List<CartItem> cartItemsList = cart.getCartItemList();
        Optional<CartItem> cartItem = cart.getCartItemList()
                .stream()
                .filter(ci -> ci.getId().equals(cartItemId)).findFirst();
        if (cartItem.isEmpty()) {
            throw new ResourceNotFoundException("CartItem not found");
        }

        cartItemsList.remove(cartItem.get());

        if (Objects.isNull(cart.getCartItemList()) || cart.getCartItemList().isEmpty()) {
            user.setCart(null);
            userService.saveUser(user);
            return null;
        }

        cart.setCartItemList(cartItemsList);
        cart = calculatePrice(cart);
        cart = cartRepository.save(cart);
        return cartResponseConverter.apply(cart);
    }

    @Override
    public boolean confirmCart(ConfirmCartRequest confirmCartRequest) {
        Cart dbCart = userService.getUser().getCart();
        if (Objects.isNull(dbCart)) {
            return false;
        }
        List<CartItem> dbCartItemsList = dbCart.getCartItemList();
        List<CartItemDTO> cartItemsList = confirmCartRequest.getCartItems();
        if (dbCartItemsList.size() != cartItemsList.size()) {
            return false;
        }

        for (int i = 0; i < dbCartItemsList.size(); i++) {
            if (!dbCartItemsList.get(i).getId().equals(cartItemsList.get(i).getId()) &&
                    !dbCartItemsList.get(i).getAmount().equals(cartItemsList.get(i).getAmount()) &&
                    !dbCartItemsList.get(i).getProductVariant().getId().equals(cartItemsList.get(i).getId())) {
                return false;
            }
        }

        if (dbCart.getTotalPrice().equals(confirmCartRequest.getTotalPrice()) &&
                dbCart.getTotalCargoPrice().equals(confirmCartRequest.getTotalCargoPrice()) &&
                dbCart.getTotalCartPrice().equals(confirmCartRequest.getTotalCartPrice())) {
            if (Objects.nonNull(dbCart.getDiscount()) && Objects.nonNull(confirmCartRequest.getDiscount())) {
                return dbCart.getDiscount().getDiscountPercent().equals(confirmCartRequest.getDiscount().getDiscountPercent());
            }
            return Objects.isNull(dbCart.getDiscount()) && Objects.isNull(confirmCartRequest.getDiscount());
        }
        return false;
    }

    @Override
    public void emptyCart() {
        User user = userService.getUser();
        user.setCart(null);
        userService.saveUser(user);
    }

    @Override
    public Cart getCart() {
        return userService.getUser().getCart();
    }


    @Override
    public void saveCart(Cart cart) {
        if (Objects.isNull(cart)) {
            throw new InvalidArgumentException("Cart is null");
        }
        cartRepository.save(cart);
    }

    @Override
    public Cart calculatePrice(Cart cart) {
        cart.setTotalCartPrice(0F);
        cart.setTotalCargoPrice(0F);
        cart.setTotalPrice(0F);

        cart.getCartItemList().forEach(cartItem -> {
            cart.setTotalCartPrice(cart.getTotalCartPrice() + (cartItem.getProductVariant().getPrice()) * cartItem.getAmount());
            cart.setTotalCargoPrice(cart.getTotalCargoPrice() + (cartItem.getProductVariant().getCargoPrice()) * cartItem.getAmount());
            cart.setTotalPrice(
                    cart.getTotalPrice() +
                            (cartItem.getProductVariant().getPrice() + cartItem.getProductVariant().getCargoPrice()) * cartItem.getAmount());
        });

        if (Objects.nonNull(cart.getDiscount())) {
            cart.setTotalPrice(cart.getTotalPrice() - ((cart.getTotalPrice() * cart.getDiscount().getDiscountPercent()) / 100));
        }

        cart.setTotalPrice(roundTwoDecimals(cart.getTotalPrice()));
        cart.setTotalCargoPrice(roundTwoDecimals(cart.getTotalCargoPrice()));
        return cart;
    }

    private float roundTwoDecimals(float d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Float.parseFloat(twoDForm.format(d));
    }

    private Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cart;
    }
}
