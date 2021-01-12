package com.rochards.beerstock.service;

import com.rochards.beerstock.entity.Beer;
import com.rochards.beerstock.repository.BeerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BeerService {

    private final BeerRepository beerRepository;

    public List<Beer> listAll() {
        return beerRepository.findAll();
    }
}
