package com.example.scalars;

import java.util.List;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository repository;
    
    @QueryMapping
    public List<Product> allProducts() {
        return repository.findAll();
    }
}
