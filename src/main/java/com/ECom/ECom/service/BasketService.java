package com.ECom.ECom.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.ECom.ECom.entity.Basket;
import com.ECom.ECom.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BasketService {

    private final RedisTemplate<String, Object> redisTemplate;

    private String getBasketKey(String buyerEmail) {
        return "basket:" + buyerEmail;
    }

    public Basket getBasket(String buyerEmail) {
        Basket basket = (Basket) redisTemplate.opsForValue().get(getBasketKey(buyerEmail));
        if (basket == null) {
            throw new ResourceNotFoundException("No basket found for email: " + buyerEmail);
        }
        return basket;
    }

    public void updateBasket(Basket basket) {

        redisTemplate.opsForValue().set(
                getBasketKey(basket.getBuyerEmail()),
                basket,
                24,
                TimeUnit.HOURS);
                
    }

    public void deleteBasket(String buyerEmail) {
        redisTemplate.delete(getBasketKey(buyerEmail));
    }
}
