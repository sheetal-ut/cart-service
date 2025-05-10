package com.ps.cartservice.model;

import lombok.Data;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "carts")
@Data
public class Cart {
    @Id
    private String cartId;
    private Long userId;  // To link to user-service
    private List<CartItem> items;
    private double totalPrice;

}
