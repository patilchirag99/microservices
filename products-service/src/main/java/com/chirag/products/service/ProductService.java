package com.chirag.products.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.chirag.products.dto.ProductRequest;
import com.chirag.products.dto.ProductResponse;
import com.chirag.products.entity.Product;
import com.chirag.products.repository.ProductsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Component
public class ProductService {
	
	@Autowired
	ProductsRepository productsRepository;
	
	public void createProduct(ProductRequest productRequest) {
		Product product = Product.builder().name(productRequest.getName())
		.desc(productRequest.getDesc())
		.price(productRequest.getPrice()).build();
		productsRepository.save(product);
		log.info("Product {} is saved", product.getId());
		
	}

	public List<ProductResponse> getAllProducts() {
			List<Product> product = productsRepository.findAll();
			return product.stream().map(this::mapToProductResponse).toList();		
		
	}

	private ProductResponse mapToProductResponse(Product product) {
		return ProductResponse.builder()
				.id(product.getId())
				.name(product.getName())
				.price(product.getPrice())
				.desc(product.getDesc())
				.build();
	}
}
