package com.chirag.orderservice.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.chirag.orderservice.service.OrderService;
import com.chirag.orderservice.dto.OrderRequest;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
@TimeLimiter(name="inventory")
@Retry(name = "inventory")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest) {
		return CompletableFuture.supplyAsync(() ->  orderService.placeOrder(orderRequest));
	}

	public CompletableFuture<String>  fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException){
		return CompletableFuture.supplyAsync(() -> "Oops! Something went wrong, please try again after sometime");
	}
	
}
