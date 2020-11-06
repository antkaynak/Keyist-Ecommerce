package com.commerce.backend.service;

import com.commerce.backend.converter.order.OrderResponseConverter;
import com.commerce.backend.dao.OrderRepository;
import com.commerce.backend.error.exception.InvalidArgumentException;
import com.commerce.backend.error.exception.ResourceFetchException;
import com.commerce.backend.model.entity.*;
import com.commerce.backend.model.request.order.PostOrderRequest;
import com.commerce.backend.model.response.order.OrderResponse;
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
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserService userService;

    @Mock
    private CartService cartService;

    @Mock
    private OrderResponseConverter orderResponseConverter;

    private Faker faker;

    private User user;

    @BeforeEach
    public void setUp() {
        faker = new Faker();
        user = new User();
    }


    @Test
    void it_should_get_all_orders_count() {

        // given
        List<Order> orders = Stream.generate(Order::new)
                .limit(faker.number().randomDigitNotZero())
                .collect(Collectors.toList());

        given(userService.getUser()).willReturn(user);
        given(orderRepository.countAllByUser(user)).willReturn(Optional.of(orders.size()));

        // when
        Integer ordersCount = orderService.getAllOrdersCount();

        // then
        then(ordersCount).isEqualTo(orders.size());

    }

    @Test
    void it_should_throw_exception_when_error() {

        // given
        given(userService.getUser()).willReturn(user);
        given(orderRepository.countAllByUser(user)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> orderService.getAllOrdersCount())
                .isInstanceOf(ResourceFetchException.class)
                .hasMessage("An error occurred whilst fetching orders count");

    }

    @Test
    void it_should_get_all_orders() {

        // given
        Integer page = faker.number().randomDigitNotZero();
        Integer pageSize = faker.number().randomDigitNotZero();

        Order order = new Order();

        List<Order> orders = Stream.generate(() -> order)
                .limit(faker.number().randomDigitNotZero())
                .collect(Collectors.toList());

        OrderResponse orderResponse = new OrderResponse();

        given(userService.getUser()).willReturn(user);
        given(orderRepository.findAllByUserOrderByDateDesc(user, PageRequest.of(page, pageSize))).willReturn(orders);
        given(orderResponseConverter.apply(any(Order.class))).willReturn(orderResponse);

        // when
        List<OrderResponse> orderResponseList = orderService.getAllOrders(page, pageSize);

        // then
        then(orderResponseList.size()).isEqualTo(orders.size());
        orderResponseList.forEach(orderResponse1 -> then(orderResponse1).isEqualTo(orderResponse));

    }

    @Test
    void it_should_post_an_order() {

        // given
        ProductVariant productVariant = new ProductVariant();
        productVariant.setSellCount(faker.number().randomDigitNotZero());
        productVariant.setStock(faker.number().randomDigitNotZero());
        Cart cart = new Cart();
        CartItem cartItem = new CartItem();
        cartItem.setProductVariant(productVariant);
        cartItem.setAmount(productVariant.getStock() - 1);
        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(cartItem);
        cart.setCartItemList(cartItemList);
        cart.setTotalPrice((float) faker.number().randomNumber());
        cart.setTotalCargoPrice((float) faker.number().randomNumber());

        user.setCart(cart);

        PostOrderRequest postOrderRequest = new PostOrderRequest();
        postOrderRequest.setShipName(faker.name().firstName());


        OrderResponse orderResponseExpected = new OrderResponse();

        Order order = new Order();

        ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);

        given(userService.getUser()).willReturn(user);
        given(orderRepository.save(orderArgumentCaptor.capture())).willReturn(order);
        given(orderResponseConverter.apply(order)).willReturn(orderResponseExpected);

        // when
        OrderResponse orderResponseResult = orderService.postOrder(postOrderRequest);

        // then
        verify(orderRepository).save(orderArgumentCaptor.getValue());
        verify(orderResponseConverter).apply(order);

        then(orderResponseResult).isEqualTo(orderResponseExpected);

    }


    @Test
    void it_should_throw_exception_when_post_order_has_null_cart() {

        // given
        given(userService.getUser()).willReturn(user);

        // when, then
        assertThatThrownBy(() -> orderService.postOrder(new PostOrderRequest()))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessage("Cart is not valid");

    }

    @Test
    void it_should_throw_exception_when_post_order_has_out_of_stock_item() {

        // given
        Cart cart = new Cart();
        CartItem cartItem = new CartItem();
        ProductVariant productVariant = new ProductVariant();
        productVariant.setStock(faker.number().randomDigitNotZero());
        cartItem.setProductVariant(productVariant);
        cartItem.setAmount(productVariant.getStock() + faker.number().randomDigitNotZero());

        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(cartItem);
        cart.setCartItemList(cartItemList);

        user.setCart(cart);

        given(userService.getUser()).willReturn(user);

        // when, then
        assertThatThrownBy(() -> orderService.postOrder(new PostOrderRequest()))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessage("A product in your cart is out of stock.");

    }
}