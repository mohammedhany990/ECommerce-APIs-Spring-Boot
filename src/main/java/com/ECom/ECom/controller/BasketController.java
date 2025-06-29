package com.ECom.ECom.controller;


import com.ECom.ECom.entity.Basket;
import com.ECom.ECom.helper.ApiResponse;
import com.ECom.ECom.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/baskets")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    @GetMapping("/{email}")
    public ResponseEntity<ApiResponse<Basket>> getBasket(@PathVariable String email) {
        Basket basket = basketService.getBasket(email);
        ApiResponse<Basket> response = ApiResponse.<Basket>builder()
                .success(true)
                .message("Basket fetched successfully")
                .data(basket)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Basket>> updateBasket(@RequestBody Basket basket) {
        basketService.updateBasket(basket);
        ApiResponse<Basket> response = ApiResponse.<Basket>builder()
                .success(true)
                .message("Basket updated successfully")
                .data(basket)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<ApiResponse<Void>> deleteBasket(@PathVariable String email) {
        basketService.deleteBasket(email);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Basket deleted successfully")
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
}