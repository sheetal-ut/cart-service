package com.ps.cartservice.controller;

import com.ps.cartservice.model.Cart;
import com.ps.cartservice.model.CartItem;
import com.ps.cartservice.service.CartCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartCommandController {
    @Autowired
    private CartCommandService cartService;

    // Create a new cart for a user
    @PostMapping
    public ResponseEntity<Cart> createCart(@RequestParam Long userId) {
        Cart cart = cartService.createCart(userId);
        return ResponseEntity.ok(cart);
    }

    // Add product to cart
    @PostMapping("/{cartId}")
    public ResponseEntity<Cart> addProductToCart(@PathVariable String cartId,
                                                 @RequestBody CartItem item) {
        Cart cart = cartService.addProductToCart(cartId, item);
        return ResponseEntity.ok(cart);
    }

    // Remove product from cart
    @DeleteMapping("/{cartId}/{productId}")
    public ResponseEntity<Cart> removeProductFromCart(@PathVariable String cartId,
                                                      @PathVariable Long productId) {
        Cart cart = cartService.removeProductFromCart(cartId, productId);
        return ResponseEntity.ok(cart);
    }

    // Update product quantity
    @PutMapping("/{cartId}/update")
    public ResponseEntity<Cart> updateProductQuantity(@PathVariable String cartId,
                                                      @RequestParam Long productId,
                                                      @RequestParam int quantity) {
        Cart cart = cartService.updateProductQuantity(cartId, productId, quantity);
        return ResponseEntity.ok(cart);
    }

    // Checkout cart
    @PostMapping("/{cartId}/checkout")
    public ResponseEntity<String> checkoutCart(@PathVariable String cartId) {
        cartService.checkoutCart(cartId);
        return ResponseEntity.ok("Cart checked out and cleared successfully.");
    }
}
