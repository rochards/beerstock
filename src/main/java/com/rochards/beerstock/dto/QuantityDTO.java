package com.rochards.beerstock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor // validation doesn't work without this annotation.
public class QuantityDTO {

    @NotNull
    private int quantity;
}
