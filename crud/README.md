# CRUD Example
This project is based on [Dan Vega's](https://www.danvega.dev) videos about Spring Boot with [GraphQL](https://www.youtube.com/watch?v=AgSO3rcSuHE) and [testing](https://www.youtube.com/watch?v=0b0x3C_BTT8).

## Project Overview
This project presents a complete GraphQL CRUD with Spring Boot for GraphQL. The CRUD consists of a five operations for coffee management. [`Coffee`](./src/main/java/com/example/crud/Coffee.java) is a Java _record_ representing a coffee. It has a sequencial id, name and size. The [coffee size](./src/main/java/com/example/crud/Size.java) is implemented as a Java _enum_, representing possible sizes for a coffee.

Management operations are implemented by the [`CoffeeService`](./src/main/java/com/example/crud/CoffeeService.java) class. The `CoffeeService` is a Spring bean annoted with `org.springframework.stereotype.Service`. The class set a list of existing coffees, simulating a persistent database. The snippet below shows how the list is set.

```java
// ...

private List<Coffee> coffees = new ArrayList<>();

// ...

@PostConstruct
private void init() {
    coffees.add(new Coffee(id.incrementAndGet(), "Caffè Americano", Size.GRANDE));
    coffees.add(new Coffee(id.incrementAndGet(), "Caffè Latte", Size.VENTI));
    coffees.add(new Coffee(id.incrementAndGet(), "Caffè Caramel Macchiato", Size.TALL));
}
```

CRUD methods on `CoffeeService` just manipulate elements in the list. 

[`CoffeeController`](./src/main/java/com/example/crud/CoffeeController.java) uses Spring for GraphQL to set an API for accessing `CoffeeService` operations. `CoffeeController` just acts as a wrapper, delegating to `CoffeeService`. The code snippet below shows two methods.

```java
// ...

@QueryMapping // (1)
public Optional<Coffee> findOne(@Argument Integer id) { // (2)
    return service.findOne(id); // (3)
}

@MutationMapping // (1)
public Coffee create(@Argument String name, @Argument Size size) { // (2)
    return service.create(name, size); // (3)
}

// ...
```

1. Map the method and the GraphQL schema type. Notice the mapping is automatic as the method has the same name as the schema type.
2. Map the method parameter and the schema type parameter.
3. Delegate behaviour to `CoffeeService` operations.

[`CoffeeControllerTest`](./src/test/java/com/example/crud/CoffeeControllerTest.java) tests the five operations in the `CoffeeController` class. It uses `org.springframework.graphql.test.tester.GraphQlTester` for testing GraphQL mappings disregarding the transport type used (e.g., HTTP, WebSocket). The class uses the tradicional [test slicing technique](https://rieckpil.de/spring-boot-test-slices-overview-and-usage/) to load only necessary context for testing GraphQL. In this example, we also had to load the `CoffeeService` as it has the management operations behaviour. The code snippet below shows the `CoffeeControllerTest` definition.

```java
// ...

@GraphQlTest(CoffeeController.class) // (1)
@Import(CoffeeService.class) // (2)
public class CoffeeControllerTest {

    @Autowired
    private GraphQlTester tester; // (3)

    @Autowired
    private CoffeeService service;

// ...
```

1. Load only necessary context for GraphQL testing.
2. Load the `CoffeeService` service class in the context.
3. Inject the object for testing GraphQL types.

Each test consists of three steps: 
1. Setting the schema to be tested;
2. Requesting the operation against the schema;
3. Asserting the result.

The code snippet below shows the `findAll` operation.

```java
String document = """
    query {
        findAll {
            id
            name
            size
        }
    }
"""; // (1)

tester
    .document(document)
    .execute() // (2)
    .path("findAll")
    .entityList(Coffee.class)
    .hasSize(3); // (3)
```

The code snippet below shows the `create` operation.

```java
String document = """
        mutation create($name: String, $size: Size) {
            create (name: $name, size: $size) {
                id
                name
                size
            }
        }
    """; // (1)

    int previousCoffeeCount = service.findAll().size();

    tester
        .document(document)
        .variable("name", "Caffè Latte")
        .variable("size", Size.GRANDE)
        .execute() // (2)
        .path("create")
        .entity(Coffee.class)
        .satisfies(coffee -> { // (3)
            assertNotNull(coffee.id());
            assertEquals("Caffè Latte", coffee.name());
            assertEquals(Size.GRANDE, coffee.size());
        });

    assertEquals(
        previousCoffeeCount + 1, 
        service.findAll().size()); // (3)
}
```

## Security
Dan Vega [published another video](https://www.youtube.com/watch?v=PkhsQPPFgOo) extending the previous example by adding security into the project.

These are the main changes:

1. Added dependencies. We need the Spring Security (obviously), but not so obvious, we also need the Web dependency. That is because the original project used WebFlux as base for the project. The WebFlux dependency lacks the `Filter` interface, necessary for implementing the `SecurityFilterChain`. Checkout the POM.xml for details.

```xml
...
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
...
```

2. Created [`SecurityConfig`](../crud/src/main/java/com/example/crud/SecurityConfig.java) class. This class is responsible for: (i) security settings; (ii) authentication mechanism; and (iii) authorisation rules. The configuration used here does not differ from traditional Spring Security used in Web projects.

3. Added method security into [`CoffeController`](../crud/src/main/java/com/example/crud/CoffeeController.java) class. 

Firstly, we secured the `findAll()` method with role based security. Therefore, only users with the `USER` role can call the `findAll()` method.

```java
...

@Secured("ROLE_USER")
@QueryMapping
public List<Coffee> findAll() {
    return service.findAll();
}

...

```

Secondly, we used a newer resource, the `PreAuthorize` annotation to add expressions-based rules.

```java
...

@PreAuthorize("hasRole('ADMIN')")
@MutationMapping
public Coffee create(@Argument String name, @Argument Size size) {
    return service.create(name, size);
}

...
```

## Run the project
In order to run the project with annecdotal data, one can use the GraphiQL. Notice the "i" in the name. This is a query editor that one can use from the browser. 

Open your browser on `http://localhost:8080/graphiql?path=/graphql` to run the following mutation as an example.

> **Notice** that we just added security into this project. Therefore, you gonna be asked for credentials. You can use _admin_ as username, and _test123_ as password.

```
query {
  findAll {
    id 
    name
    size
  }
}

```