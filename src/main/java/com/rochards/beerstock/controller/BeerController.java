package com.rochards.beerstock.controller;

import com.rochards.beerstock.dto.BeerDTO;
import com.rochards.beerstock.entity.Beer;
import com.rochards.beerstock.service.BeerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
        BeerDTO beerDTO = beerService.findById(id);
        if (beerDTO != null) {
            return ResponseEntity.ok(beerDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<BeerDTO> findByName(@PathVariable String name) {
        BeerDTO beerDTO = beerService.findByName(name);
        if (beerDTO != null) {
            return ResponseEntity.ok(beerDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<BeerDTO> create(@Valid @RequestBody BeerDTO beerDTO) {
        BeerDTO createdBeer = beerService.create(beerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBeer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        beerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
