package com.rochards.beerstock.exception.type;

public class BeerNotFoundException extends RuntimeException {
    public BeerNotFoundException(Long id) {
        super(String.format("Beer with id '%d' not found.", id));
    }
}
