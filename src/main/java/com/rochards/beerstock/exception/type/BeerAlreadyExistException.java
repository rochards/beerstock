package com.rochards.beerstock.exception.type;

public class BeerAlreadyExistException extends RuntimeException {
    public BeerAlreadyExistException(String beerName) {
        super(String.format("Beer with name '%s' already exist in the system.", beerName));
    }
}
