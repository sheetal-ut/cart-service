package com.ps.cartservice.kafka;

import com.ps.cartservice.entity.Cart;
import com.ps.cartservice.entity.CartEvent;
import com.ps.cartservice.entity.CartItem;
import com.ps.cartservice.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CartEventConsumer {

    @Autowired
    private CartRepository cartRepository;

    @KafkaListener(topics = "${cart.kafka.topic}", groupId = "cart-query-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumeCartEvent(CartEvent event) {
        if ("CART_CHECKED_OUT".equalsIgnoreCase(event.getEventType())) {
            String cartId = event.getCart().getCartId();
            cartRepository.deleteById(cartId);
            System.out.println("Deleted cart after CHECKOUT event: " + cartId);
        } else {
            Cart cart = new Cart();
            cart.setCartId(event.getCart().getCartId());
            cart.setUserId(event.getCart().getUserId());
            cart.setTotalPrice(event.getCart().getTotalPrice());

            cart.setItems(
                    event.getCart().getItems().stream().map(i -> {
                        CartItem item = new CartItem();
                        item.setProductId(i.getProductId());
                        item.setName(i.getName());
                        item.setType(i.getType());
                        item.setBrand(i.getBrand());
                        item.setPrice(i.getPrice());
                        item.setQuantity(i.getQuantity());
                        item.setCart(cart);
                        return item;
                    }).collect(Collectors.toList())
            );

            cartRepository.save(cart);
            System.out.println("Consumed event: " + event.getEventType());
        }
    }
}
