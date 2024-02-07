package com.chirag.inventoryservice.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.chirag.inventoryservice.dto.InventoryResponse;
import com.chirag.inventoryservice.service.InventoryService;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
	
	@Autowired
	private InventoryService inventoryService;
	
	//Using Path variable
	//http://localhost:9004/api/inventory/iphone_13,iphone_13_red
	
	//Using request param - we will be using this
	//http://localhost:9004/api/inventory?skuCode=iphone_13&&skuCodeiphone_13_red
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode) {
		return inventoryService.checkInStock(skuCode);
	}
	
}
