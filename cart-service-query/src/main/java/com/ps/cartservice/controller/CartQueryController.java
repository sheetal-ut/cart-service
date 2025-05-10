package com.ps.cartservice.controller;

import com.ps.cartservice.entity.Cart;
import com.ps.cartservice.service.CartQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carts")
public class CartQueryController {
    @Autowired
    private CartQueryService cartQueryService;

    @GetMapping("/{cartId}")
    public Cart getCartByCartId(@PathVariable("cartId") String cartId){
        return cartQueryService.getCartById(cartId);
    }

    @GetMapping("/user/{userId}")
    public Cart getCartByUserId(@PathVariable("userId") Long userId){
        return cartQueryService.getCartByUserId(userId);
    }

}
