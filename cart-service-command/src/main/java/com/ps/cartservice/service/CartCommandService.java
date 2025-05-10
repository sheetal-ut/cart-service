package com.ps.cartservice.service;

import com.ps.cartservice.kafka.CartEventProducer;
import com.ps.cartservice.model.Cart;
import com.ps.cartservice.model.CartEvent;
import com.ps.cartservice.model.CartItem;
import com.ps.cartservice.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartCommandService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartEventProducer cartEventProducer;

    // Create a new cart for a user (if not already exists)
    public Cart createCart(Long userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setItems(new ArrayList<>());
        cart.setTotalPrice(0.0);
        Cart newCart = cartRepository.save(cart);

        CartEvent cartEvent = new CartEvent("CART_CREATED", newCart);
        cartEventProducer.sendCartEvent(cartEvent);

        return newCart;
    }

    public Cart addProductToCart(String cartId, CartItem item) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> items = cart.getItems();

        Optional<CartItem> existingItem = items.stream()
                .filter(p -> p.getProductId().equals(item.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem ci = existingItem.get();
            ci.setQuantity(ci.getQuantity() + item.getQuantity());
        } else {
            items.add(item);
        }

        updateTotalPrice(cart);

        Cart newCart = cartRepository.save(cart);
        CartEvent cartEvent = new CartEvent("PRODUCT_ADDED", newCart);
        cartEventProducer.sendCartEvent(cartEvent);

        return newCart;
    }

    // Remove product from cart
    public Cart removeProductFromCart(String cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().removeIf(p -> p.getProductId().equals(productId));

        updateTotalPrice(cart);

        Cart newCart = cartRepository.save(cart);
        CartEvent cartEvent = new CartEvent("PRODUCT_REMOVED", newCart);
        cartEventProducer.sendCartEvent(cartEvent);

        return newCart;
    }

    // Update product quantity
    public Cart updateProductQuantity(String cartId, Long productId, int newQuantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().forEach(p -> {
            if (p.getProductId().equals(productId)) {
                p.setQuantity(newQuantity);
            }
        });

        updateTotalPrice(cart);

        Cart newCart = cartRepository.save(cart);
        CartEvent cartEvent = new CartEvent("PRODUCT_UPDATED", newCart);
        cartEventProducer.sendCartEvent(cartEvent);

        return newCart;
    }

    // Checkout cart (just clears the cart for now)
    public void checkoutCart(String cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cartRepository.delete(cart);

        CartEvent cartEvent = new CartEvent("CART_CHECKED_OUT", cart);
        cartEventProducer.sendCartEvent(cartEvent);
    }

    // Utility to recalculate total price
    private void updateTotalPrice(Cart cart) {
        double total = cart.getItems().stream()
                .mapToDouble(p -> p.getPrice() * p.getQuantity())
                .sum();
        cart.setTotalPrice(total);
    }


}
