package com.chirag.inventoryservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.chirag.inventoryservice.entity.Inventory;
import com.chirag.inventoryservice.repository.InventoryRepository;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
		
		return args -> {
			Inventory inventory = new Inventory();
			inventory.setSkuCode("iphone_13");
			inventory.setQuantity(100);
			
			Inventory inventory1 = new Inventory();
			inventory1.setSkuCode("iphone_13_red");
			inventory1.setQuantity(0);
			
			Inventory inventory2 = new Inventory();
			inventory2.setSkuCode("iphone_13_black");
			inventory2.setQuantity(10);
			
			inventoryRepository.save(inventory);
			inventoryRepository.save(inventory1);
			inventoryRepository.save(inventory2);
		};
	}
}
