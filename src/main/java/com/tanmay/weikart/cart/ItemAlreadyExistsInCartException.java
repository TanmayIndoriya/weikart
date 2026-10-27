package com.tanmay.weikart.cart;

public class ItemAlreadyExistsInCartException extends RuntimeException {
    public ItemAlreadyExistsInCartException(String s) {
        super(s);
    }
}
