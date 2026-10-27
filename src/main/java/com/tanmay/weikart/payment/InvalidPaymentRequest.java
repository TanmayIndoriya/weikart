package com.tanmay.weikart.payment;

public class InvalidPaymentRequest extends RuntimeException {
    public InvalidPaymentRequest(String message) {
        super(message);
    }
}
