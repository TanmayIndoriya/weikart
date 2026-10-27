package com.tanmay.weikart.product;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String id) {
        super("Product with id '" + id + "' not found.");
    }
}
