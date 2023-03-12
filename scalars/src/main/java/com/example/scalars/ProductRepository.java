package com.example.scalars;

import org.springframework.data.repository.ListCrudRepository;

public interface ProductRepository 
    extends ListCrudRepository<Product, Integer> {
    
}
