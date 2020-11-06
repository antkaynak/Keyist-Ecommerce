package com.commerce.backend.service;

import com.commerce.backend.converter.cart.CartResponseConverter;
import com.commerce.backend.dao.CartRepository;
import com.commerce.backend.error.exception.InvalidArgumentException;
import com.commerce.backend.error.exception.ResourceNotFoundException;
import com.commerce.backend.model.entity.*;
import com.commerce.backend.model.response.cart.CartResponse;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class CartServiceImplTest {

    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    @Mock
    private CartResponseConverter cartResponseConverter;

    private User user;

    private Faker faker;


    @BeforeEach
    public void setUp() {
        user = new User();
        faker = new Faker();
    }

    @Test
    void it_should_add_to_cart_when_cart_is_empty() {

        // given
        Long productVariantId = faker.number().randomNumber();
        Integer amount = faker.number().randomDigitNotZero();

        Cart cart = new Cart();
        user.setCart(cart);
        cart.setUser(user);

        ProductVariant productVariant = new ProductVariant();
        productVariant.setPrice((float) faker.number().randomNumber());
        productVariant.setCargoPrice((float) faker.number().randomNumber());
        productVariant.setStock(amount + 1);

        CartResponse cartResponseExpected = new CartResponse();

        given(userService.getUser()).willReturn(user);
        given(productService.findProductVariantById(productVariantId)).willReturn(productVariant);
        given(cartRepository.save(cart)).willReturn(cart);
        given(cartResponseConverter.apply(cart)).willReturn(cartResponseExpected);

        // when
        CartResponse cartResponseResult = cartService.addToCart(productVariantId, amount);

        // then
        then(cartResponseResult).isEqualTo(cartResponseExpected);

    }

    @Test
    void it_should_add_to_cart_when_cart_is_null() {

        // given
        Long productVariantId = faker.number().randomNumber();
        Integer amount = faker.number().randomDigitNotZero();

        Cart cart = new Cart();

        ProductVariant productVariant = new ProductVariant();
        productVariant.setPrice((float) faker.number().randomNumber());
        productVariant.setCargoPrice((float) faker.number().randomNumber());
        productVariant.setStock(amount + 1);

        CartResponse cartResponseExpected = new CartResponse();

        given(userService.getUser()).willReturn(user);
        given(productService.findProductVariantById(productVariantId)).willReturn(productVariant);
        given(cartRepository.save(any(Cart.class))).willReturn(cart);
        given(cartResponseConverter.apply(cart)).willReturn(cartResponseExpected);

        // when
        CartResponse cartResponseResult = cartService.addToCart(productVariantId, amount);

        // then
        then(cartResponseResult).isEqualTo(cartResponseExpected);

    }

    @Test
    void it_should_add_to_cart_when_cart_already_have_same_product() {

        // given
        Long productVariantId = faker.number().randomNumber();
        Integer amount = faker.number().randomDigitNotZero();

        Cart cart = new Cart();

        user.setCart(cart);
        cart.setUser(user);

        ProductVariant productVariant = new ProductVariant();
        productVariant.setId(productVariantId);
        productVariant.setPrice((float) faker.number().randomNumber());
        productVariant.setCargoPrice((float) faker.number().randomNumber());
        productVariant.setStock(amount + 1);

        List<CartItem> cartItemList = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItem.setProductVariant(productVariant);
        cartItem.setAmount(productVariant.getStock() - amount);
        cartItemList.add(cartItem);
        cart.setCartItemList(cartItemList);

        CartResponse cartResponseExpected = new CartResponse();

        given(userService.getUser()).willReturn(user);
        given(cartRepository.save(cart)).willReturn(cart);
        given(cartResponseConverter.apply(cart)).willReturn(cartResponseExpected);

        // when
        CartResponse cartResponseResult = cartService.addToCart(productVariantId, amount);

        // then

        then(cartResponseResult).isEqualTo(cartResponseExpected);

    }

    @Test
    void it_should_throw_exception_when_cart_already_have_product_and_no_stock() {

        // given
        Long productVariantId = faker.number().randomNumber();
        Integer amount = faker.number().randomDigitNotZero();

        Cart cart = new Cart();

        user.setCart(cart);
        cart.setUser(user);

        ProductVariant productVariant = new ProductVariant();
        productVariant.setId(productVariantId);
        productVariant.setPrice((float) faker.number().randomNumber());
        productVariant.setCargoPrice((float) faker.number().randomNumber());
        productVariant.setStock(amount + 1);

        List<CartItem> cartItemList = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItem.setProductVariant(productVariant);
        cartItem.setAmount(productVariant.getStock() + amount);
        cartItemList.add(cartItem);
        cart.setCartItemList(cartItemList);

        given(userService.getUser()).willReturn(user);

        // when, then

        assertThatThrownBy(() -> cartService.addToCart(productVariantId, amount))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessage("Product does not have desired stock.");

    }

    @Test
    void it_should_throw_exception_when_cart_is_null_and_no_stock() {

        // given
        Long productVariantId = faker.number().randomNumber();
        Integer amount = faker.number().randomDigitNotZero();

        ProductVariant productVariant = new ProductVariant();
        productVariant.setStock(amount - 1);

        given(userService.getUser()).willReturn(user);
        given(productService.findProductVariantById(productVariantId)).willReturn(productVariant);

        // when, then

        assertThatThrownBy(() -> cartService.addToCart(productVariantId, amount))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessage("Product does not have desired stock.");

    }

    @Test
    void it_should_increment_cart_item() {

        // given
        Long cartItemId = faker.number().randomNumber();
        Integer amount = faker.number().randomDigitNotZero();

        Cart cart = new Cart();
        user.setCart(cart);
        cart.setUser(user);

        ProductVariant productVariant = new ProductVariant();
        productVariant.setPrice((float) faker.number().randomNumber());
        productVariant.setCargoPrice((float) faker.number().randomNumber());
        productVariant.setStock(amount + 1);

        List<CartItem> cartItemList = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setProductVariant(productVariant);
        cartItem.setAmount(productVariant.getStock() - amount);
        cartItemList.add(cartItem);
        cart.setCartItemList(cartItemList);

        CartResponse cartResponseExpected = new CartResponse();

        given(userService.getUser()).willReturn(user);
        given(cartRepository.save(cart)).willReturn(cart);
        given(cartResponseConverter.apply(cart)).willReturn(cartResponseExpected);

        // when
        CartResponse cartResponseResult = cartService.incrementCartItem(cartItemId, amount);

        // then
        then(cartResponseResult).isEqualTo(cartResponseExpected);

    }


    @Test
    void it_should_throw_exception_when_increment_and_no_stock() {

        // given
        Long cartItemId = faker.number().randomNumber();
        Integer amount = faker.number().randomDigitNotZero();

        Cart cart = new Cart();
        user.setCart(cart);
        cart.setUser(user);

        ProductVariant productVariant = new ProductVariant();
        productVariant.setStock(amount - 1);

        List<CartItem> cartItemList = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setProductVariant(productVariant);
        cartItem.setAmount(productVariant.getStock());
        cartItemList.add(cartItem);
        cart.setCartItemList(cartItemList);

        given(userService.getUser()).willReturn(user);

        // when, then
        assertThatThrownBy(() -> cartService.incrementCartItem(cartItemId, amount))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessage("Product does not have desired stock.");

    }

    @Test
    void it_should_throw_exception_when_increment_and_no_cart_item() {

        // given
        Long cartItemId = faker.number().randomNumber();
        Integer amount = faker.number().randomDigitNotZero();

        Cart cart = new Cart();
        user.setCart(cart);
        cart.setUser(user);
        List<CartItem> cartItemList = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId + 1);
        cartItemList.add(cartItem);
        cart.setCartItemList(cartItemList);

        given(userService.getUser()).willReturn(user);

        // when, then
        assertThatThrownBy(() -> cartService.incrementCartItem(cartItemId, amount))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("CartItem not found");

    }

    @Test
    void it_should_throw_exception_when_increment_and_no_cart() {

        // given
        Long cartItemId = faker.number().randomNumber();
        Integer amount = faker.number().randomDigitNotZero();

        given(userService.getUser()).willReturn(user);

        // when, then
        assertThatThrownBy(() -> cartService.incrementCartItem(cartItemId, amount))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Empty cart");

    }


    @Test
    void it_should_decrement_cart_item() {

        // given
        Long cartItemId = faker.number().randomNumber();
        Integer amount = faker.number().randomDigitNotZero();

        Cart cart = new Cart();
        user.setCart(cart);
        cart.setUser(user);

        ProductVariant productVariant = new ProductVariant();
        productVariant.setPrice((float) faker.number().randomNumber());
        productVariant.setCargoPrice((float) faker.number().randomNumber());

        List<CartItem> cartItemList = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setProductVariant(productVariant);
        cartItem.setAmount(amount + faker.number().randomDigitNotZero());
        cartItemList.add(cartItem);
        cart.setCartItemList(cartItemList);

        CartResponse cartResponseExpected = new CartResponse();

        given(userService.getUser()).willReturn(user);
        given(cartRepository.save(cart)).willReturn(cart);
        given(cartResponseConverter.apply(cart)).willReturn(cartResponseExpected);

        // when
        CartResponse cartResponseResult = cartService.decrementCartItem(cartItemId, amount);

        // then
        then(cartResponseResult).isEqualTo(cartResponseExpected);

    }

    @Test
    void it_should_decrement_cart_item_and_empty_cart() {

        // given
        Long cartItemId = faker.number().randomNumber();
        Integer amount = faker.number().randomDigitNotZero();

        Cart cart = new Cart();
        user.setCart(cart);
        cart.setUser(user);

        ProductVariant productVariant = new ProductVariant();
        productVariant.setPrice((float) faker.number().randomNumber());
        productVariant.setCargoPrice((float) faker.number().randomNumber());

        List<CartItem> cartItemList = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setProductVariant(productVariant);
        cartItem.setAmount(amount);
        cartItemList.add(cartItem);
        cart.setCartItemList(cartItemList);

        given(userService.getUser()).willReturn(user);
        given(userService.saveUser(user)).willReturn(user);

        // when
        CartResponse cartResponseResult = cartService.decrementCartItem(cartItemId, amount);

        // then
        then(cartResponseResult).isEqualTo(null);

    }

    @Test
    void it_should_decrement_cart_item_and_remove_cart_item() {

        // given
        Long cartItemId = faker.number().randomNumber();
        Integer amount = faker.number().randomDigitNotZero();

        Cart cart = new Cart();
        user.setCart(cart);
        cart.setUser(user);

        ProductVariant productVariant = new ProductVariant();
        productVariant.setPrice((float) faker.number().randomNumber());
        productVariant.setCargoPrice((float) faker.number().randomNumber());

        List<CartItem> cartItemList = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setProductVariant(productVariant);
        cartItem.setAmount(amount);
        cartItemList.add(cartItem);

        CartItem cartItemOther = new CartItem();
        cartItemOther.setId(cartItemId + 1);
        cartItemOther.setProductVariant(productVariant);
        cartItemOther.setAmount(faker.number().randomDigitNotZero());
        cartItemList.add(cartItemOther);

        cart.setCartItemList(cartItemList);
        CartResponse cartResponseExpected = new CartResponse();

        given(userService.getUser()).willReturn(user);
        given(cartRepository.save(cart)).willReturn(cart);
        given(cartResponseConverter.apply(cart)).willReturn(cartResponseExpected);

        // when
        CartResponse cartResponseResult = cartService.decrementCartItem(cartItemId, amount);

        // then
        then(cartResponseResult).isEqualTo(cartResponseExpected);

    }


    @Test
    void it_should_throw_exception_when_decrement_and_no_cart_item() {

        // given
        Long cartItemId = faker.number().randomNumber();
        Integer amount = faker.number().randomDigitNotZero();

        Cart cart = new Cart();
        user.setCart(cart);
        cart.setUser(user);
        List<CartItem> cartItemList = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId + 1);
        cartItemList.add(cartItem);
        cart.setCartItemList(cartItemList);

        given(userService.getUser()).willReturn(user);

        // when, then
        assertThatThrownBy(() -> cartService.decrementCartItem(cartItemId, amount))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("CartItem not found");

    }

    @Test
    void it_should_throw_exception_when_decrement_and_no_cart() {

        // given
        Long cartItemId = faker.number().randomNumber();
        Integer amount = faker.number().randomDigitNotZero();

        given(userService.getUser()).willReturn(user);

        // when, then
        assertThatThrownBy(() -> cartService.decrementCartItem(cartItemId, amount))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Empty cart");

    }

    @Test
    void it_should_fetch_cart_when_cart_exists() {

        // given
        Cart cart = new Cart();
        user.setCart(cart);
        CartResponse cartResponseExpected = new CartResponse();

        given(userService.getUser()).willReturn(user);
        given(cartResponseConverter.apply(cart)).willReturn(cartResponseExpected);


        // when
        CartResponse cartResponseResult = cartService.fetchCart();


        // then
        then(cartResponseResult).isEqualTo(cartResponseExpected);

    }

    @Test
    void it_should_return_null_cart_when_cart_does_not_exist() {

        // given
        given(userService.getUser()).willReturn(user);

        // when
        CartResponse cartResponseResult = cartService.fetchCart();

        // then
        then(cartResponseResult).isEqualTo(null);

    }


    @Test
    void it_should_remove_from_cart() {

        // given
        Long cartItemId = faker.number().randomNumber();

        Cart cart = new Cart();
        user.setCart(cart);
        cart.setUser(user);

        ProductVariant productVariant = new ProductVariant();
        productVariant.setPrice((float) faker.number().randomNumber());
        productVariant.setCargoPrice((float) faker.number().randomNumber());

        List<CartItem> cartItemList = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setProductVariant(productVariant);
        cartItem.setAmount(faker.number().randomDigitNotZero());
        cartItemList.add(cartItem);

        CartItem cartItemOther = new CartItem();
        cartItemOther.setId(cartItemId + 1);
        cartItemOther.setProductVariant(productVariant);
        cartItemOther.setAmount(faker.number().randomDigitNotZero());
        cartItemList.add(cartItemOther);

        cart.setCartItemList(cartItemList);


        CartResponse cartResponseExpected = new CartResponse();

        given(userService.getUser()).willReturn(user);
        given(cartRepository.save(cart)).willReturn(cart);
        given(cartResponseConverter.apply(cart)).willReturn(cartResponseExpected);

        // when
        CartResponse cartResponseResult = cartService.removeFromCart(cartItemId);

        // then
        then(cartResponseResult).isEqualTo(cartResponseExpected);

    }


    @Test
    void it_should_remove_from_cart_and_empty_cart() {

        // given
        Long cartItemId = faker.number().randomNumber();

        Cart cart = new Cart();
        user.setCart(cart);
        cart.setUser(user);

        ProductVariant productVariant = new ProductVariant();
        productVariant.setPrice((float) faker.number().randomNumber());
        productVariant.setCargoPrice((float) faker.number().randomNumber());

        List<CartItem> cartItemList = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setProductVariant(productVariant);
        cartItem.setAmount(faker.number().randomDigitNotZero());
        cartItemList.add(cartItem);

        cart.setCartItemList(cartItemList);

        given(userService.getUser()).willReturn(user);
        given(userService.saveUser(user)).willReturn(user);

        // when
        CartResponse cartResponseResult = cartService.removeFromCart(cartItemId);

        // then
        then(cartResponseResult).isEqualTo(null);

    }

    @Test
    void it_should_throw_exception_when_remove_from_cart_and_no_cart_item() {

        // given
        Long cartItemId = faker.number().randomNumber();

        Cart cart = new Cart();
        user.setCart(cart);
        cart.setUser(user);
        List<CartItem> cartItemList = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId + 1);
        cartItemList.add(cartItem);
        cart.setCartItemList(cartItemList);

        given(userService.getUser()).willReturn(user);

        // when, then
        assertThatThrownBy(() -> cartService.removeFromCart(cartItemId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("CartItem not found");

    }

    @Test
    void it_should_throw_exception_when_remove_from_cart_and_no_cart() {

        // given
        Long cartItemId = faker.number().randomNumber();

        given(userService.getUser()).willReturn(user);

        // when, then
        assertThatThrownBy(() -> cartService.removeFromCart(cartItemId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Cart or CartItem not found");

    }


    @Test
    void it_should_empty_cart() {

        // given
        user.setCart(new Cart());

        given(userService.getUser()).willReturn(user);

        // when
        cartService.emptyCart();

        // then
        then(user.getCart()).isEqualTo(null);

    }

    @Test
    void it_should_get_cart() {

        // given
        Cart cart = new Cart();
        user.setCart(cart);

        given(userService.getUser()).willReturn(user);

        // when
        Cart cartResult = cartService.getCart();

        // then
        then(cartResult).isEqualTo(cart);

    }

    @Test
    void it_should_get_cart_when_it_is_null() {

        // given
        given(userService.getUser()).willReturn(user);

        // when
        Cart cartResult = cartService.getCart();

        // then
        then(cartResult).isEqualTo(null);

    }

    @Test
    void it_should_calculate_price() {

        // given
        DecimalFormat twoDForm = new DecimalFormat("#.##");

        Float totalCartPrice = (float) faker.number().randomNumber();
        Float totalCargoPrice = (float) faker.number().randomNumber();
        Float totalPrice = totalCartPrice + totalCargoPrice;

        Cart cart = new Cart();
        cart.setTotalCartPrice(totalCartPrice);
        cart.setTotalCargoPrice(totalCargoPrice);
        cart.setTotalPrice(totalPrice);

        ProductVariant productVariant = new ProductVariant();
        productVariant.setCargoPrice((float) faker.number().randomDigitNotZero());
        productVariant.setPrice((float) faker.number().randomDigitNotZero());

        CartItem cartItem = new CartItem();
        cartItem.setAmount(faker.number().randomDigitNotZero());
        cartItem.setProductVariant(productVariant);

        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(cartItem);

        cart.setCartItemList(cartItemList);


        // when
        Cart cartResult = cartService.calculatePrice(cart);

        // then
        Float totalPriceExpected = Float.parseFloat(twoDForm.format((productVariant.getPrice() + productVariant.getCargoPrice()) * cartItem.getAmount()));
        then(cartResult.getTotalPrice()).isEqualTo(totalPriceExpected);
        then(cartResult.getTotalCartPrice()).isEqualTo(Float.parseFloat(twoDForm.format(productVariant.getPrice() * cartItem.getAmount())));
        then(cartResult.getTotalCargoPrice()).isEqualTo(Float.parseFloat(twoDForm.format(productVariant.getCargoPrice() * cartItem.getAmount())));

    }

    @Test
    void it_should_calculate_price_with_discount() {

        // given
        DecimalFormat twoDForm = new DecimalFormat("#.##");

        Float totalCartPrice = (float) faker.number().randomNumber();
        Float totalCargoPrice = (float) faker.number().randomNumber();
        Float totalPrice = totalCartPrice + totalCargoPrice;

        Cart cart = new Cart();
        cart.setTotalCartPrice(totalCartPrice);
        cart.setTotalCargoPrice(totalCargoPrice);
        cart.setTotalPrice(totalPrice);
        Discount discount = new Discount();
        discount.setDiscountPercent(faker.number().numberBetween(1, 100));
        cart.setDiscount(discount);

        ProductVariant productVariant = new ProductVariant();
        productVariant.setCargoPrice((float) faker.number().randomDigitNotZero());
        productVariant.setPrice((float) faker.number().randomDigitNotZero());

        CartItem cartItem = new CartItem();
        cartItem.setAmount(faker.number().randomDigitNotZero());
        cartItem.setProductVariant(productVariant);

        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(cartItem);

        cart.setCartItemList(cartItemList);


        // when
        Cart cartResult = cartService.calculatePrice(cart);

        // then
        Float totalPriceExpected = Float.parseFloat(twoDForm.format((productVariant.getPrice() + productVariant.getCargoPrice()) * cartItem.getAmount()));
        Float totalPriceDiscountExpected = Float.parseFloat(twoDForm.format(totalPriceExpected - totalPriceExpected * discount.getDiscountPercent() / 100));
        then(cartResult.getTotalPrice()).isEqualTo(totalPriceDiscountExpected);
        then(cartResult.getTotalCartPrice()).isEqualTo(Float.parseFloat(twoDForm.format(productVariant.getPrice() * cartItem.getAmount())));
        then(cartResult.getTotalCargoPrice()).isEqualTo(Float.parseFloat(twoDForm.format(productVariant.getCargoPrice() * cartItem.getAmount())));

    }

    @Test
    void it_should_save_cart() {

        // given
        Cart cart = new Cart();

        ArgumentCaptor<Cart> cartArgumentCaptor = ArgumentCaptor.forClass(Cart.class);
        given(cartRepository.save(cart)).willReturn(cart);

        // when
        cartService.saveCart(cart);

        //then
        verify(cartRepository).save(cartArgumentCaptor.capture());
        then(cart).isEqualTo(cartArgumentCaptor.getValue());

    }

    @Test
    void it_should_throw_exception_when_save_null_cart() {

        // when, then
        assertThatThrownBy(() -> cartService.saveCart(null))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessage("Cart is null");

    }
}