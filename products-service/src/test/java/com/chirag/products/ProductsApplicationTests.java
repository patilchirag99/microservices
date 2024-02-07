package com.chirag.products;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.chirag.products.dto.ProductRequest;
import com.chirag.products.repository.ProductsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.assertions.Assertions;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductsApplicationTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
	
	//Json to pojo and pojo to json
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	ProductsRepository productsRepository;
	
	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);	
	}
	
	
	
	@Test
	void shouldCreateProduct() throws Exception{
		ProductRequest req =  getProductRequest();
		String reqStr=  objectMapper.writeValueAsString(req);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(reqStr))
			.andExpect(MockMvcResultMatchers.status().isCreated());
		Assertions.assertTrue(productsRepository.findAll().size() == 1) ;
	}



	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("Iphone 13")
				.desc("iphone 13")
				.price(BigDecimal.valueOf(1200))
				.build();
	}

}
