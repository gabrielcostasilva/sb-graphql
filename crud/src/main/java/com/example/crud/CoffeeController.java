package com.example.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class CoffeeController {
    
    private final CoffeeService service;

    public CoffeeController(final CoffeeService service) {
        this.service = service;
    }

    @QueryMapping
    public List<Coffee> findAll() {
        return service.findAll();
    }

    @QueryMapping
    public Optional<Coffee> findOne(@Argument Integer id) {
        return service.findOne(id);
    }

    @MutationMapping
    public Coffee create(@Argument String name, @Argument Size size) {
        return service.create(name, size);
    }

    @MutationMapping
    public Coffee update(@Argument Integer id, @Argument String name, @Argument Size size) {
        return service.update(id, name, size);
    }

    @MutationMapping
    public Coffee delete(@Argument Integer id) {
        return service.delete(id);
    }
}
