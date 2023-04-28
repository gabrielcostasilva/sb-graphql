package com.gabrielcostasilva.graphqlclient;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GraphqlClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(GraphqlClientApplication.class, args);
	}

	@Bean
	CommandLineRunner clr(CountryService service, CountryRepository repository) {
		return args -> {
			service.getCountries().subscribe(repository::saveAll);
		};
	}

}
