package com.tanmay.weikart.order;

import com.tanmay.weikart.cart.CartItem;
import com.tanmay.weikart.product.Product;
import com.tanmay.weikart.product.ProductNotFoundException;
//import com.tanmay.weikart.product.ProductRepository;
import com.tanmay.weikart.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderMapper {

    public OrderResponse toOrderResponse(Order savedOrder) {
        OrderResponse response = new OrderResponse();
        response.setId(savedOrder.getOrderId());
        response.setStatus(savedOrder.getStatus());
        response.setPaymentStatus(savedOrder.getOrderPaymentStatus());
        response.setOrderTotal(savedOrder.getSubTotal());
        return response;
    }

    public OrderItem mapCartItemToOrderItem(CartItem cartItem,Order newOrder) {
        return OrderItem.builder()
                .product(cartItem.getProduct())
                .order(newOrder)
                .priceAtPurchase(cartItem.getProduct().getPrice())
                .quantity(cartItem.getQuantity())
                .itemTotal(cartItem.getItemTotal())
                .build();
    }
}
