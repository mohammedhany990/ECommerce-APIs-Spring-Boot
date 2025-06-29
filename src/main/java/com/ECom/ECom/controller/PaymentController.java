package com.ECom.ECom.controller;


import com.ECom.ECom.dto.ConfirmPaymentRequest;
import com.ECom.ECom.entity.Basket;
import com.ECom.ECom.helper.ApiResponse;
import com.ECom.ECom.service.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    @PostMapping
    public ResponseEntity<ApiResponse<Basket>> createOrUpdatePayment(@RequestParam String buyerEmail) {
        Basket basket = paymentService.createOrUpdatePaymentIntent(buyerEmail);
        if (basket == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<Basket>builder()
                            .success(false)
                            .message("Basket not found for email: " + buyerEmail)
                            .data(null)
                            .build());
        }
        return ResponseEntity.ok(ApiResponse.<Basket>builder()
                .success(true)
                .message("Payment intent created/updated successfully")
                .data(basket)
                .build());
    }

    @PostMapping("/confirm-payment")
    public ResponseEntity<ApiResponse<Basket>> confirmPayment(@RequestBody ConfirmPaymentRequest request) {
        Basket basket = paymentService.confirmPayment(request.getBuyerEmail(), request.getPaymentIntentId());
        if (basket == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<Basket>builder()
                            .success(false)
                            .message("Payment confirmation failed")
                            .data(null)
                            .build());
        }
        return ResponseEntity.ok(ApiResponse.<Basket>builder()
                .success(true)
                .message("Payment confirmed successfully")
                .data(basket)
                .build());
    }

    @PostMapping("/webhook")
    public ResponseEntity<ApiResponse<String>> handleWebhook(HttpServletRequest request) throws IOException {
        String payload = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        String stripeSignature = request.getHeader("Stripe-Signature");

        try {

            Event event = Webhook.constructEvent(payload, stripeSignature, webhookSecret);

            paymentService.handleWebhookEvent(event);

            return ResponseEntity.ok(ApiResponse.<String>builder()
                    .success(true)
                    .message("Webhook processed successfully")
                    .data(null)
                    .build());
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<String>builder()
                            .success(false)
                            .message("Invalid Stripe signature: " + e.getMessage())
                            .data(null)
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<String>builder()
                            .success(false)
                            .message("Webhook processing failed: " + e.getMessage())
                            .data(null)
                            .build());
        }
    }
}