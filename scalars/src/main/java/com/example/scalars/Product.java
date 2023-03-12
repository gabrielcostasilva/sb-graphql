package com.example.scalars;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Product {
    
    @Id @GeneratedValue
    private Integer id;
    private String title;
    private boolean isOnSale;
    private double weight; 
    private BigDecimal price;
    private LocalDateTime dateCreated;

    public Product(
            String title, 
            boolean isOnSale, 
            double weight,
            BigDecimal price,
            LocalDateTime dateCreated) {

        this.title = title;
        this.isOnSale = isOnSale;
        this.weight = weight;
        this.price = price;
        this.dateCreated = dateCreated;
    }
}
