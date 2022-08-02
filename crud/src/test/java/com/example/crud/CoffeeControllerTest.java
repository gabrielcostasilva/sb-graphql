package com.example.crud;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;

@GraphQlTest(CoffeeController.class)
@Import(CoffeeService.class)
public class CoffeeControllerTest {

    @Autowired
    private GraphQlTester tester;

    @Autowired
    private CoffeeService service;
    
    @Test
    public void findAllCoffeeShouldReturnAllCoffees() {

        String document = """
            query {
                findAll {
                    id
                    name
                    size
                }
            }
        """;

        tester
            .document(document)
            .execute()
            .path("findAll")
            .entityList(Coffee.class)
            .hasSize(3);
    }

    @Test
    public void findOneCoffeeSholdReturnSingleCoffee() {
        
        String document = """
            query findOne($id: ID){
                findOne(id: $id) {
                    id
                    name
                    size
                }
            }
        """;

        tester
            .document(document)
            .variable("id", 1)
            .execute()
            .path("findOne")
            .entity(Coffee.class)
            .satisfies(coffee -> {
                assertEquals("Caffè Americano", coffee.name());
                assertEquals(Size.GRANDE, coffee.size());
            });
    }

    @Test
    public void createCoffeeShouldReturnCreatedCoffeee() {

        String document = """
            mutation create($name: String, $size: Size) {
                create (name: $name, size: $size) {
                    id
                    name
                    size
                }
            }
        """;

        int previousCoffeeCount = service.findAll().size();

        tester
            .document(document)
            .variable("name", "Caffè Latte")
            .variable("size", Size.GRANDE)
            .execute()
            .path("create")
            .entity(Coffee.class)
            .satisfies(coffee -> {
                assertNotNull(coffee.id());
                assertEquals("Caffè Latte", coffee.name());
                assertEquals(Size.GRANDE, coffee.size());
            });

        assertEquals(
            previousCoffeeCount + 1, 
            service.findAll().size());
    }

    @Test
    public void updateCoffeeShouldReturnUpdatedCoffeee() {

        String document = """
            mutation update($id: ID!, $name: String, $size: Size) {
                update (id: $id, name: $name, size: $size) {
                    id
                    name
                    size
                }
            }
        """;

        tester
            .document(document)
            .variable("id", 1)
            .variable("name", "NEW Caffè Latte")
            .variable("size", Size.SHORT)
            .execute()
            .path("update")
            .entity(Coffee.class);

            Coffee updatedCoffee = service.findOne(1).get();
            assertEquals("NEW Caffè Latte", updatedCoffee.name());
            assertEquals(Size.SHORT, updatedCoffee.size());
    }

    @Test
    public void deleteCoffeeShouldReturnDeletedCoffee() {

        String document = """
            mutation delete($id: ID!) {
                delete(id: $id) {
                    id
                    name
                    size
                }
            }
        """;

        int currentCoffeeCount = service.findAll().size();

        tester
            .document(document)
            .variable("id", 1)
            .executeAndVerify();

        assertEquals(
            currentCoffeeCount - 1, 
            service.findAll().size());
    }

}
