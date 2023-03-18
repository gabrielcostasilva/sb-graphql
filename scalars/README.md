# GRAPHQL EXAMPLES - SCALARS
This repo groups few examples of GraphQL operations implemented with Spring Boot.

**This folder introduces GraphQL custom Scalars**. The project in this folder is based on [Dan Vega's introduction to custom Scalars in Java]( https://www.youtube.com/watch?v=ooknmgr4WiA).

> Please, checkout the root repository for a full understanding of GraphQL with Spring Boot.

## Overview
The project uses the well-known [MVC pattern](https://developer.mozilla.org/en-US/docs/Glossary/MVC) with [Spring Boot](https://spring.io/projects/spring-boot) to build a [`Product`](./src/main/java/com/example/scalars/Product.java) management API with GraphQL. 

Currently, the [`ProductController`](./src/main/java/com/example/scalars/ProductController.java) has one single method that lists all `Products`.

The novelty in this project is the use of custom [Scalars](https://www.graphql-java.com/documentation/scalars/). Scalars are analogous to data types in programming languages. GraphQL specification defines five native Scalars: String, Boolean, Float, Int, and ID.

GraphQL can relate some data types to its native Scalars. For example, Java `double` is automatically mapped to GraphQL `Float`. However, programming languages, like Java, go beyond GraphQL five native types. For instance, the code snippet below shows the attribute list of `Product`. 

```java
(...)

private Integer id;
private String title;
private boolean isOnSale;
private double weight; 
private BigDecimal price;
private LocalDateTime dateCreated;

(...)
```

Both `BigDecimal` and `LocalDataTime` are data types, but there is no direct mapping for them in GraphQL specification. This is a case for [using custom Scalars](https://www.graphql-java.com/documentation/scalars/#writing-your-own-custom-scalars).

Although one can extend the native GraphQL Scalar list by implementing custom Scalars, there are libraries that do exactly this.

This project uses [Extended Scalars for graphql-java library](https://github.com/graphql-java/graphql-java-extended-scalars), which provides implementation for several Java data types.

In addition to add the dependency to our `pom.xml`, we need a [custom configuration bean](./src/main/java/com/example/scalars/GraphQLConfig.java). We also need to declare the custom scalar in our [schema](./src/main/resources/graphql/schema.graphqls).

Unfortunately, Extended Scalars library does not provide an implementation for Java `LocalDateTime`. Therefore, we also bring [graphql-java-datetime](https://github.com/tailrocks/graphql-java-datetime) library into the project. 

Unlike Extended Scalars, graphql-java-datetime library does not require a configuration bean to work.

## Dependencies
This project adds [Extended Scalars for graphql-java library](https://github.com/graphql-java/graphql-java-extended-scalars) and [graphql-java-datetime](https://github.com/tailrocks/graphql-java-datetime) dependencies.

