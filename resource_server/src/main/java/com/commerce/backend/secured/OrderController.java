package com.commerce.backend.secured;

import com.commerce.backend.model.Order;
import com.commerce.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
public class OrderController extends SecuredApiController {


    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(value = "/order/count", method = RequestMethod.GET)
    public ResponseEntity getAllOrdersCount(Principal principal) {
        Integer orderCount = orderService.getAllOrdersCount(principal);
        return new ResponseEntity<Integer>(orderCount, HttpStatus.OK);
    }

    @RequestMapping(value = "/order", method = RequestMethod.GET, params = {"page", "size"})
    public ResponseEntity getAllOrders(@RequestParam("page") Integer page, @RequestParam("size") Integer pageSize, Principal principal) {
        System.out.println("In getAllOrders GET");
        List<Order> orderList = orderService.getAllOrders(principal, page, pageSize);
        return new ResponseEntity<List<Order>>(orderList, HttpStatus.OK);
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public ResponseEntity postOrder(@Valid @RequestBody Order order, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Order saveOrder = orderService.postOrder(principal, order);
        return new ResponseEntity<Order>(saveOrder, HttpStatus.OK);
    }


}
