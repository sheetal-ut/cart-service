package com.ps.cartservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
public class CartServiceCommandApplication {

	public static void main(String[] args) {
		SpringApplication.run(CartServiceCommandApplication.class, args);
	}

}
