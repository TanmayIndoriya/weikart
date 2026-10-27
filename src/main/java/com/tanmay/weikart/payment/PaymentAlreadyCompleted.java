package com.tanmay.weikart.payment;

public class PaymentAlreadyCompleted extends RuntimeException {
    public PaymentAlreadyCompleted(String paymentId) {
        super("Payment with ID " + paymentId + " already done.");
    }
}
