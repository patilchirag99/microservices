package com.chirag.products.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.chirag.products.entity.Product;

@Repository
public interface ProductsRepository extends MongoRepository<Product, String>{

}
