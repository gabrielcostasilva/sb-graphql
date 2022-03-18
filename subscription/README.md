# GraphQL Example
This project is based on Josh Long's excellent intro to [Spring tips: Spring GraphQL](https://www.youtube.com/watch?v=kVSYVhmvNCI) and a previous [example](../mutation/).

This project extends the previous one by implementing subscription. A subscription is the terminology in GraphQL for receiving data streams.

## Project Overview
As in the previous project, the change starts from the schema, adding an entry for subscription.

```graphql
(...)

type Subscription {
    customerEvents (customerId: ID): CustomerEvent
}

type CustomerEvent {
    customer: Customer
    event: CustomerEventType
}

enum CustomerEventType {
    UPDATED
    DELETED
}

(...)
```

Notice the _subscription_ introduces a new type, `CustomerEvent`, which is also described in the schema. In turn, `CustomerEvent` also introduces a new type, `CustomerEventType`, as an `enum`.

To match the schema, we created a new method in our controller (`GraphQLController.customerEvents()`), and new also new classes to represent the necessary types (`com.example.graphql.CustomerEvent` and `com.example.graphql.CustomerEventType`).

As before, the method is annotated to represent the GraphQL subscription (`org.springframework.graphql.data.method.annotation.SubscriptionMapping`), as the code snippet below shows.

```java
// (...)
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

// (...)
```
The method body just simulates a continuous stream of `CustomerEvent`s every second, 10 times. 

A subscription does not have to be served over HTTP. Therefore, in this example, we use WebSocket. To enable WebSocket, we need to add `spring.graphql.websocket.path=/graphql` to the `application.properties` configuration file. 

That would be all we need. However, there is no simple way to access WebSocket connections. Therefore, a [webpage](./src/main/resources/static/index.html) was developed.

The webpage uses [graphql-ws](https://github.com/enisdenjo/graphql-ws), a JS library that enables GraphQL over WebSocket, to subscribe and receive data.


### Run the project
The webpage code access the stream as soon as the page is open. However, the results are sent to JS console. Therefore, one need to open devtools to see the result. 