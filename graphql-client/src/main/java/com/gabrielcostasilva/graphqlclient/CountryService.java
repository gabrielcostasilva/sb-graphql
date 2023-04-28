package com.gabrielcostasilva.graphqlclient;

import java.util.List;

import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class CountryService {
    
    private final HttpGraphQlClient graphQLClient;

    public CountryService() {
        WebClient webclient = WebClient.builder()
                                    .baseUrl("https://countries.trevorblades.com")
                                    .build();

        graphQLClient = HttpGraphQlClient.builder(webclient).build();
    }

    public Mono<List<Country>> getCountries() {
        String document = """
            query {
                countries {
                    name
                    emoji
                    currency
                    code
                    capital
                }
            }
        """;

        return graphQLClient.document(document)
            .retrieve("countries")
            .toEntityList(Country.class);
    }
}
