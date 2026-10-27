package com.tanmay.weikart.cart;

public class CartItemNotFound extends RuntimeException{
    public CartItemNotFound(String s) {
        super(s);
    }
}
