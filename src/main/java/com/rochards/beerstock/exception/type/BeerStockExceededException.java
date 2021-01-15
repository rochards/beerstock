package com.rochards.beerstock.exception.type;

public class BeerStockExceededException extends RuntimeException {
    public BeerStockExceededException(String message) {
        super(message);
    }
}
