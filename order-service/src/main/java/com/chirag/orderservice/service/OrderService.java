package com.chirag.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.chirag.orderservice.event.OrderPlacedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.chirag.orderservice.entity.Order;
import com.chirag.orderservice.entity.OrderLineItems;
import com.chirag.orderservice.dto.InventoryResponse;
import com.chirag.orderservice.dto.OrderLineItemsDto;
import com.chirag.orderservice.dto.OrderRequest;
import com.chirag.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
	
	@Autowired
	private final OrderRepository orderRepository;
	
	private final WebClient.Builder webClientBuilder;

	private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
	
	public String placeOrder(OrderRequest orderRequest) {
		
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		
		List<OrderLineItems>  orderLineitems = orderRequest.getOrderLineItemsDtoList()
		.stream()
		.map(this::maptoDto).toList();
		
		order.setOrderLineItemsList(orderLineitems);
		
		List<String > skucodes =  order.getOrderLineItemsList().stream().map(orderLineItem -> orderLineItem.getSkuCode()).toList();
		
		//Check stock from inventory service before placing order
		InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
				.uri("http://inventory-service/api/inventory" ,
						uriBuilder -> uriBuilder.queryParam("skuCode", skucodes).build())
				.retrieve()
				.bodyToMono(InventoryResponse[].class)
				.block(); //Syncronous req as webclient by default is async
		
		//Check if all the isInStock values are true
		boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);
		
		if(allProductsInStock) {
			orderRepository.save(order);
			kafkaTemplate.send("notificationTopic",  new OrderPlacedEvent(order.getOrderNumber()));
			return "Order placed successfully";
		}
		else throw new IllegalArgumentException("Product is not in stock please try again later");
		
	}

	private OrderLineItems maptoDto(OrderLineItemsDto orderLineItemsDto) {
		OrderLineItems orderLineItems = new OrderLineItems();
		orderLineItems.setPrice(orderLineItemsDto.getPrice());
		orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
		orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
		return orderLineItems;
	}
}
