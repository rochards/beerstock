package com.rochards.beerstock.service;

import com.rochards.beerstock.dto.BeerDTO;
import com.rochards.beerstock.entity.Beer;
import com.rochards.beerstock.exception.type.BeerAlreadyExistException;
import com.rochards.beerstock.exception.type.BeerNotFoundException;
import com.rochards.beerstock.exception.type.BeerStockExceededException;
import com.rochards.beerstock.mapper.BeerMapper;
import com.rochards.beerstock.repository.BeerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper = BeerMapper.INSTANCE;

    public List<BeerDTO> listAll() {
        return beerRepository.findAll()
                .stream().map(beerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<BeerDTO> findById(Long id) {
        Optional<Beer> beer = beerRepository.findById(id);
        return beer.map(beerMapper::toDTO);
    }

    public Optional<BeerDTO> findByName(String beerName) {
        Optional<Beer> beer = beerRepository.findByName(beerName);
        return beer.map(beerMapper::toDTO);
    }

    public BeerDTO create(BeerDTO beerDTO) {
        checkIfAlreadyExist(beerDTO.getName());

        Beer beer = beerMapper.toModel(beerDTO);
        Beer createdBeer = beerRepository.save(beer);

        return beerMapper.toDTO(createdBeer);
    }

    public void delete(Long id) {
        checkIfAlreadyExist(id);
        beerRepository.deleteById(id);
    }

    public BeerDTO incrementStock(Long id, int quantityToIncrement) {
        Optional<Beer> optBeer = beerRepository.findById(id);
        if (optBeer.isPresent()) {
            Beer beerToIncrement = optBeer.get();

            int finalQuantity = beerToIncrement.getQuantity() + quantityToIncrement;
            if (finalQuantity <= beerToIncrement.getMax()) {
                beerToIncrement.setQuantity(finalQuantity);
                return beerMapper.toDTO(beerRepository.save(beerToIncrement));
            }

            throw new BeerStockExceededException(String.format("Cannot increment '%d' on beer with id '%d' because it" +
                    " already has '%d' and '%d' is the maximum.", quantityToIncrement, id,
                    beerToIncrement.getQuantity(), beerToIncrement.getMax()));
        }

        throw new BeerNotFoundException(id);
    }

    public BeerDTO decrementStock(Long id, int quantityToDecrement) {
        Optional<Beer> optBeer = beerRepository.findById(id);
        if (optBeer.isPresent()) {
            Beer beerToDecrement = optBeer.get();

            int finalQuantity = beerToDecrement.getQuantity() - quantityToDecrement;
            if (finalQuantity >= 0) {
                beerToDecrement.setQuantity(finalQuantity);
                return beerMapper.toDTO(beerRepository.save(beerToDecrement));
            }

            throw new BeerStockExceededException(String.format("Cannot decrement '%d' on beer with id '%d' because it" +
                    " only has '%d' and '0' is the minimum.", quantityToDecrement, id, beerToDecrement.getQuantity()));
        }

        throw new BeerNotFoundException(id);
    }

    private void checkIfAlreadyExist(String beerName) {
        Optional<Beer> beer = beerRepository.findByName(beerName);
        if (beer.isPresent()) {
            throw new BeerAlreadyExistException(beerName);
        }
    }

    private void checkIfAlreadyExist(Long id) {
        boolean exists = beerRepository.existsById(id);
        if (!exists) {
            throw new BeerNotFoundException(id);
        }
    }

}
