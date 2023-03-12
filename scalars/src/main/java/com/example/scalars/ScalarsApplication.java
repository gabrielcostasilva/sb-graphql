package com.example.scalars;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ScalarsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScalarsApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(ProductRepository repo) {
		return args -> {
			repo.saveAll(List.of(
					new Product("Rice", true, 22.3, new BigDecimal(2.99), LocalDateTime.now()),
					new Product("Beans", false, 8.9, new BigDecimal(3.99), LocalDateTime.now()),
					new Product("Fish", true, 26.7, new BigDecimal(4.99), LocalDateTime.now()))).stream()
					.forEach(System.out::println);
		};
	}

}
