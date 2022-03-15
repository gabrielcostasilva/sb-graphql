package com.example.graphql;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class GraphQLController {

    private List<Customer> db = 
        new ArrayList<>(){{
            add(new Customer(1, "John Doe"));
            add(new Customer(2, "Anna Doe"));
        }};
    
    @SchemaMapping(typeName = "Query", field = "customers")
    public Flux<Customer> customers() {
        return Flux.fromIterable(db);
    }

    @QueryMapping
    public Flux<Customer> customersByName(@Argument String name) {
        return Flux.fromIterable(db).filter(item -> item.name().equals(name));
    }

    @SchemaMapping(typeName = "Customer")
    public List<Order> orders(Customer customer) {
        return List.of(new Order(1, customer.id()), new Order(2, customer.id()), new Order(3, customer.id()));
    }

    @MutationMapping
    public Mono<Customer> addCustomer (@Argument String name) {
        var customer = new Customer(this.db.size() + 1, name);
        this.db.add(customer);

        return Mono.just(customer);
    }

    @SubscriptionMapping
    Flux<CustomerEvent> customerEvents(@Argument Integer customerId) {

        Optional<Customer> customer = db.stream()
                            .filter(aCustomer -> aCustomer.id() == customerId)
                            .findAny();

        Mono<Customer> monoCustomer = Mono.just(customer.get());

        return monoCustomer
                    .flatMapMany(aCustomer -> {
                        var stream = Stream.generate(() -> new CustomerEvent(aCustomer, Math.random() > .5 ? CustomerEventType.DELETED: CustomerEventType.UPDATED));

                        return Flux.fromStream(stream);
                    })
                    .delayElements(Duration.ofSeconds(1))
                    .take(10);

    }
}
