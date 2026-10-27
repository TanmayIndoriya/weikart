package com.tanmay.weikart.order;

import com.tanmay.weikart.config.PagedResponseDTO;
import com.tanmay.weikart.config.SearchRequest;
import com.tanmay.weikart.user.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
            @Valid @RequestBody OrderRequest orderRequest,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(orderService.placeOrder(user,orderRequest));
    }

//    admin can view all orders with pagination and filtering
    @GetMapping("admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PagedResponseDTO<OrderResponse>> getAllOrders(
            @Valid SearchRequest searchRequest
            ) {
        return ResponseEntity.ok(orderService.getAllOrders(searchRequest));
    }

    @GetMapping("me")
    public ResponseEntity<PagedResponseDTO<OrderResponse>> getMyOrders(
            @Valid SearchRequest searchRequest,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(orderService.getMyOrders(user,searchRequest));
    }

    @GetMapping("/items/{orderId}")
    public ResponseEntity<List<OrderItemResponse>> getOrderItems(
            @PathVariable UUID orderId
    ) {
        return ResponseEntity.ok(orderService.getOrderItems(orderId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable UUID orderId
    ) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }


    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<Map<String,String >> cancelMyOrder(
            @PathVariable UUID orderId,
            Authentication authentication
    ) {

        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(orderService.cancelOrder(user,orderId));
    }

}
