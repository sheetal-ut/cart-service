package com.ps.cartservice.service;

import com.ps.cartservice.entity.Cart;
import com.ps.cartservice.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartQueryService {
    @Autowired
    private CartRepository cartRepository;

    public Cart getCartById(String cartId) {
        return cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found with id: "+cartId));
    }

    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found for user with ID: " + userId));
    }
}
