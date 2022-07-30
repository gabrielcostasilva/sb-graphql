package com.example.crud;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

@Service
public class CoffeeService {

    private List<Coffee> coffees = new ArrayList<>();
    AtomicInteger id = new AtomicInteger(0);

    public List<Coffee> findAll() {
        return this.coffees;
    }

    public Optional<Coffee> findOne(Integer id) {
        return coffees
                    .stream()
                    .filter(coffee -> coffee.id() == id)
                    .findFirst();
    }

    public Coffee create(String name, Size size) {

        Coffee coffee = new Coffee(id.incrementAndGet(), name, size);
        coffees.add(coffee);
        return coffee;
    }

    public Coffee update(Integer id, String name, Size size) {

        Coffee updatedCoffee = new Coffee(id, name, size);
        Optional<Coffee> optional = coffees
                                        .stream()
                                        .filter(aCoffee -> aCoffee.id() == id)
                                        .findFirst();

        optional.ifPresentOrElse(aCoffee -> {
            int index = coffees.indexOf(aCoffee);
            coffees.set(index, updatedCoffee);

        }, IllegalArgumentException::new);

        return updatedCoffee;
    }

    public Coffee delete(Integer id) {
        Coffee coffee = coffees
                            .stream()
                            .filter(aCoffee -> aCoffee.id() == id)
                            .findFirst()
                            .orElseThrow(IllegalArgumentException::new);

        coffees.remove(coffee);

        return coffee;
    }

    @PostConstruct
    private void init() {
        coffees.add(new Coffee(id.incrementAndGet(), "Caffè Americano", Size.GRANDE));
        coffees.add(new Coffee(id.incrementAndGet(), "Caffè Latte", Size.VENTI));
        coffees.add(new Coffee(id.incrementAndGet(), "Caffè Caramel Macchiato", Size.TALL));
    }
    
}
