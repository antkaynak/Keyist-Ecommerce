package com.commerce.backend.service;

import com.commerce.backend.dao.OrderRepository;
import com.commerce.backend.dao.UserRepository;
import com.commerce.backend.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {


    private final OrderRepository orderRepository;
    private final UserRepository userRepository;


    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Integer getAllOrdersCount(Principal principal) {
        User user = getUserFromPrinciple(principal);
        return orderRepository.countAllByUser(user);
    }

    @Override
    public List<Order> getAllOrders(Principal principal, Integer page, Integer pageSize) {
        User user = getUserFromPrinciple(principal);
        if (page == null || page < 0) {
            throw new IllegalArgumentException("Invalid page");
        }
        return orderRepository.findAllByUserOrderByDateDesc(user, PageRequest.of(page, pageSize));
    }

    @Override
    public Order postOrder(Principal principal, Order order) {
        User user = getUserFromPrinciple(principal);
        Cart cart = user.getCart();
        if (cart == null) {
            throw new IllegalArgumentException("Cart not found");
        }
        List<CartItem> cartItems = cart.getCartItemList();

        Order saveOrder = new Order();
        saveOrder.setUser(user);
        saveOrder.setShipName(order.getShipName());
        saveOrder.setEmail(order.getEmail());
        saveOrder.setPhone(order.getPhone());
        saveOrder.setShipAddress(order.getShipAddress());
        saveOrder.setShipAddress2(order.getShipAddress2());
        saveOrder.setCity(order.getCity());
        saveOrder.setCountry(order.getCountry());
        saveOrder.setState(order.getState());
        saveOrder.setZip(order.getZip());
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        saveOrder.setDate(date);
        saveOrder.setCargoFirm(order.getCargoFirm());
        saveOrder.setOrderDetailsList(new ArrayList<>());

        for (CartItem i : cartItems) {
            //increase sell count on the product
            i.getCartProduct().setSellCount(i.getCartProduct().getSellCount() + i.getAmount());

            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setAmount(i.getAmount());
            orderDetails.setOrder(saveOrder);
            orderDetails.setProductDisplay(i.getCartProduct());
            saveOrder.getOrderDetailsList().add(orderDetails);
        }


        saveOrder.setTotalPrice(cart.getTotalPrice());
        saveOrder.setTotalCargoPrice(cart.getTotalCargoPrice());
        saveOrder.setShipped(0);

        orderRepository.save(saveOrder);
        return saveOrder;
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
