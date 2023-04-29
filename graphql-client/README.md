# GRAPHQL CLIENT
This project shows the use of a GraphQL client with Spring Boot to accessing a [public GraphQL API](https://countries.trevorblades.com).

The project in this folder is based on [Dan Vega's introduction to GraphQL client](https://youtu.be/BuPItqaVeGo).

> Please, checkout the root repository for a full understanding of GraphQL with Spring Boot.

## Overview
The project follows the traditional structure for a Web app: [`CountryService`](./src/main/java/com/gabrielcostasilva/graphqlclient/CountryService.java) -> [`CountryRepository`](./src/main/java/com/gabrielcostasilva/graphqlclient/CountryRepository.java) -> [`Country`](./src/main/java/com/gabrielcostasilva/graphqlclient/Country.java).

Whereas `Country` is Java _record_ representing a persistent Country entity, `CountryRepository` provides methods for managing the entity. Notice that Country also complies with the data structure expected from the GraphQL public API.

`CountryService` instantiates a `org.springframework.graphql.client.HttpGraphQlClient` based on a `org.springframework.web.reactive.function.client.WebClient` object. `CountryService.getCountries()` returns a list of countries as mapped to the Java _record_.

When you run the project, you see the method below in action. In a nutshell, the method retrieves and saves countries from the public API. 

```java
@Bean
CommandLineRunner clr(CountryService service, CountryRepository repository) {
    return args -> {
        service.getCountries().subscribe(repository::saveAll);
    };
}
```

## Dependencies
Checkout the [`pom.xml`](./pom.xml) to see all the dependencies. An important detail is that this project uses Spring Data JDBC rather than JPA. Although there is no particular reason for that, a direct benefit is the possibility of using `records` instead of Java entity classes.


