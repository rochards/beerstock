package com.rochards.beerstock.builder;

import com.rochards.beerstock.dto.BeerDTO;
import com.rochards.beerstock.enums.BeerType;
import lombok.Builder;

@Builder
public class BeerDTOBuilder {


    @Builder.Default private final Long id = 1L;
    @Builder.Default private final String name = "Brahma";
    @Builder.Default private final String brand = "Ambev";
    @Builder.Default private final Integer max = 50;
    @Builder.Default private final Integer quantity = 10;
    @Builder.Default private final BeerType type = BeerType.LAGER;

    public BeerDTO toBeerDTO() {
        return new BeerDTO(id, name, brand, max, quantity, type);
    }
}
