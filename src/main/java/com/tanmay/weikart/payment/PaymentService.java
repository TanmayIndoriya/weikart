
package com.tanmay.weikart.payment;

import com.tanmay.weikart.order.Order;
import com.tanmay.weikart.order.OrderNotFoundException;
import com.tanmay.weikart.order.OrderPaymentStatus;
import com.tanmay.weikart.order.OrderRepository;
import com.tanmay.weikart.order.OrderStatus;
import com.tanmay.weikart.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public PaymentResponse processOfflinePayment(
            User user,
            PaymentRequest paymentRequest
    ) {
        Order order = validatePaymentRequestBeforeProcessing(paymentRequest);

        Optional<Payment> existingPayment = paymentRepository.findByOrder(order);

        if (existingPayment.isPresent()) {
            Payment existing = existingPayment.get();

            if (existing.getPaymentStatus() == PaymentStatus.SUCCESS) {
                return new PaymentResponse(
                        existing.getPaymentId(),
                        order.getOrderId(),
                        existing.getPaymentStatus(),
                        existing.getPaymentMode()
                );
            }

            existing.setPaymentMode(PaymentMode.COD);
            existing.setPaymentStatus(PaymentStatus.SUCCESS);
            existing.setPaidAmount(existing.getTotalAmount());

            order.setOrderPaymentStatus(OrderPaymentStatus.PAYMENT_COMPLETED);
            orderRepository.save(order);

            Payment saved = paymentRepository.save(existing);

            return new PaymentResponse(
                    saved.getPaymentId(),
                    order.getOrderId(),
                    saved.getPaymentStatus(),
                    saved.getPaymentMode()
            );
        }

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setOrder(order);
        payment.setPaidAmount(order.getSubTotal());
        payment.setTotalAmount(order.getSubTotal());
        payment.setCurrency(PaymentCurrency.INR);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setPaymentMode(PaymentMode.COD);

        order.setOrderPaymentStatus(OrderPaymentStatus.PAYMENT_COMPLETED);
        orderRepository.save(order);

        Payment saved = paymentRepository.save(payment);

        return new PaymentResponse(
                saved.getPaymentId(),
                order.getOrderId(),
                saved.getPaymentStatus(),
                saved.getPaymentMode()
        );
    }

    public Order validatePaymentRequestBeforeProcessing(
            PaymentRequest paymentRequest
    ) {
        if (paymentRequest == null || paymentRequest.getOrderId() == null) {
            throw new InvalidPaymentRequest("Invalid payment request.");
        }

        if (paymentRequest.getPaymentMode() != PaymentMode.COD) {
            throw new InvalidPaymentRequest(
                    "Only cash on delivery is currently supported."
            );
        }

        Order order = orderRepository.findById(paymentRequest.getOrderId())
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                paymentRequest.getOrderId().toString()
                        )
                );

        if (order.getStatus() != OrderStatus.PLACED) {
            throw new InvalidPaymentRequest(
                    "Cannot process payment for order with status: "
                            + order.getStatus()
            );
        }

        if (order.getOrderPaymentStatus()
                != OrderPaymentStatus.PAYMENT_PENDING) {
            throw new InvalidPaymentRequest(
                    "Cannot process payment for order with payment status: "
                            + order.getOrderPaymentStatus()
            );
        }

        return order;
    }

    public String getPaymentStatus(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new InvalidPaymentRequest(
                                "Payment not found for id: " + paymentId
                        )
                );

        return payment.getPaymentStatus().toString();
    }
}
