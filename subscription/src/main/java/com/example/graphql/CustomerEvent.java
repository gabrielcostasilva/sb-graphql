package com.example.graphql;

public record CustomerEvent(Customer customer, CustomerEventType event) { }
