package com.example.graphql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;

@GraphQlTest(GraphQLController.class)
public class GraphQLControllerTest {

    @Autowired
    private GraphQlTester tester;

    @Autowired
    private GraphQLController controller;

    @Test
    public void findAllCustomers() {
        String document = """
            query {
                customers {
                    id
                    name
                    orders {
                        id
                    }
                }
            }
        """;

        tester
            .document(document)
            .execute()
            .path("customers")
            .entityList(Customer.class)
            .hasSize(2);
    }
    
    @Test
    public void findCustomerByName() {

        String document = """
            query findCustomersByName($name: String) {
                customersByName (name: $name) {
                    id
                    name
                    orders {
                        id
                    }

                }
            }
        """;

        tester
            .document(document)
            .variable("name", "John Doe")
            .execute()
            .path("customersByName")
            .entityList(Customer.class)
            .hasSize(1);
    }

    @Test
    public void addCustomer() {

        String document = """
            mutation create($name: String) {
                addCustomer(name: $name) {
                    id
                    name
                    orders {
                        id
                    }
                }
            }
        """;

        long currentCustomerCount = controller.customers().count().block();

        tester
            .document(document)
            .variable("name", "Michael J Fox")
            .execute()
            .path("addCustomer")
            .entity(Customer.class)
            .satisfies(customer -> {
                assertNotNull(customer);
                assertEquals("Michael J Fox", customer.name());
            });

            assertEquals(
                currentCustomerCount + 1, 
                controller.customers().count().block());
            
    }
}
