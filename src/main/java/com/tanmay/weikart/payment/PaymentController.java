package com.tanmay.weikart.payment;

import com.tanmay.weikart.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponse> processPayment(
            @Valid @RequestBody PaymentRequest paymentRequest,
            Authentication authentication
    ) {
        if (paymentRequest.getPaymentMode() != PaymentMode.COD) {
            throw new InvalidPaymentRequest(
                    "Only cash on delivery is currently supported."
            );
        }

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                paymentService.processOfflinePayment(user, paymentRequest)
        );
    }

    @GetMapping("/{id}/status")
    public String pollPaymentStatus(@PathVariable("id") UUID paymentId) {
        return paymentService.getPaymentStatus(paymentId);
    }
}