package com.ps.cartservice.model;

import lombok.Data;

@Data
public class CartItem {
    private Long productId;
    private String name;
    private String type;
    private String brand;
    private double price;
    private int quantity;
}
