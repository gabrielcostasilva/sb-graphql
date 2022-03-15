# GraphQL Example
This project is based on Josh Long's excellent intro to [Spring tips: Spring GraphQL](https://www.youtube.com/watch?v=kVSYVhmvNCI) and a previous [example](https://github.com/gabrielcostasilva/sb-controllers/tree/main/graphql).

This project extends the previous one by adding a mutation. A mutation is the terminology in GraphQL for adding changes to data.

## Project Overview
Starting from the [previous project](https://github.com/gabrielcostasilva/sb-controllers/tree/main/graphql), we just need to add the `mutation` operation in our schema, and add the corresponding method in our controller. The code snippet below shows the code added into the existing schema.

```graphql

type Mutation {
    addCustomer(name: String): Customer
}

```

A `Mutation` is a type, like `Query`. In our example, this type has only one operation: `addCustomer(String)`. This operation returns a `Customer` type. The type `Customer` is already defined in our schema.

The next step consists of adding the corresponding Java method into our controller, as the snippet below shows.


```java
// (...)
private List<Customer> db = 
    new ArrayList<>(){{
        add(new Customer(1, "John Doe"));
        add(new Customer(2, "Anna Doe"));
    }};

// (...)
@MutationMapping
public Mono<Customer> addCustomer (@Argument String name) {
    var customer = new Customer(this.db.size() + 1, name);
    this.db.add(customer);

    return Mono.just(customer);
}
```

Notice the first change is necessary because previous code used an immutable list. Next, we just add a method following the conventions set for the schema. In this case, the method receives an argument (`String name`) and returns a `Customer`. As we are using [WebFlux](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html), the result must be wrapped in a `Mono` container.

As for `Query`, Spring also provides a shortcut for annotating a mutation: `@MutationMapping`. The method parameter is also annotated, with `@org.springframework.graphql.data.method.annotation.Argument`.

### Run the project
In order to run the project with annecdotal data, one can use the GraphiQL. Notice the "i" in the name. This is a query editor that one can use from the browser. 

Open your browser on `http://localhost:8080/graphiql?path=/graphql` to run the following mutation as an example.

```
mutation {
  addCustomer(name: "Bibiana") {
    id, name
  }
}

```