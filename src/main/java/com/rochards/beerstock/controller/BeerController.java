package com.rochards.beerstock.controller;

import com.rochards.beerstock.dto.BeerDTO;
import com.rochards.beerstock.service.BeerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/beers")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BeerController {

    private final BeerService beerService;

    @GetMapping
    public List<BeerDTO> listAll() {
        return beerService.listAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BeerDTO create(@Valid @RequestBody BeerDTO beerDTO) {
        return beerService.create(beerDTO);
    }
}
