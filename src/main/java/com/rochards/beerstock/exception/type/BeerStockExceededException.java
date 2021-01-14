package com.rochards.beerstock.exception.type;

public class BeerStockExceededException extends RuntimeException {
    public BeerStockExceededException(Long id, int increment, int max, int current) {
        super(String.format("Cannot increment '%d' on beer with id '%d' because it already has '%d' and '%d' is the " +
                        "maximum.", increment, id, current, max));
    }
}
