package com.rochards.beerstock.dto;

import com.rochards.beerstock.enums.BeerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BeerDTO {

    private Long id;
    private String name;
    private String brand;
    private int max;
    private int quantity;

    @Enumerated(EnumType.STRING)
    private BeerType type;
}
