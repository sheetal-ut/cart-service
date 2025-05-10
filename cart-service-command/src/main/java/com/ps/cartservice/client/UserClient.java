package com.ps.cartservice.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user-service", url = "http://user-service")
public interface UserClient {
}
