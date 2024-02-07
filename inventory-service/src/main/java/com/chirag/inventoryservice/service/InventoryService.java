package com.chirag.inventoryservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chirag.inventoryservice.dto.InventoryResponse;
import com.chirag.inventoryservice.repository.InventoryRepository;

@Service
public class InventoryService {
	
	@Autowired
	private InventoryRepository inventoryRepository;
	
	@Transactional(readOnly = true)
	public List<InventoryResponse> checkInStock(List<String> skucode) {
		
		return inventoryRepository.findBySkuCodeIn(skucode).stream().map(inventory -> 
			InventoryResponse.builder()
			.skuCode(inventory.getSkuCode())
			.isInStock(inventory.getQuantity() > 0)
			.build()
		)
		.toList();
	}
}
