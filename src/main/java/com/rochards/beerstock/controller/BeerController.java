package com.rochards.beerstock.controller;

import com.rochards.beerstock.dto.BeerDTO;
import com.rochards.beerstock.dto.QuantityDTO;
import com.rochards.beerstock.entity.Beer;
import com.rochards.beerstock.service.BeerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/beers")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BeerController {

    private final BeerService beerService;

    @GetMapping
    public ResponseEntity<List<BeerDTO>> listAll() {
        List<BeerDTO> beers = beerService.listAll();
        return ResponseEntity.ok(beers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BeerDTO> findById(@PathVariable Long id) {
        Optional<BeerDTO> beerDTO = beerService.findById(id);
        return beerDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<BeerDTO> findByName(@PathVariable String name) {
        Optional<BeerDTO> beerDTO = beerService.findByName(name);
        return beerDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BeerDTO> create(@Valid @RequestBody BeerDTO beerDTO) {
        BeerDTO createdBeer = beerService.create(beerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBeer);
    }

    @PatchMapping("/{id}/increment")
    public ResponseEntity<BeerDTO> incrementStock(@PathVariable Long id, @Valid @RequestBody QuantityDTO quantityDTO) {
        BeerDTO incrementedBeerDTO = beerService.incrementStock(id, quantityDTO.getQuantity());
        return ResponseEntity.ok(incrementedBeerDTO);
    }

    @PatchMapping("/{id}/decrement")
    public ResponseEntity<BeerDTO> decrementStock(@PathVariable Long id, @Valid @RequestBody QuantityDTO quantityDTO) {
        BeerDTO decrementedBeer = beerService.decrementStock(id, quantityDTO.getQuantity());
        return ResponseEntity.ok(decrementedBeer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        beerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
