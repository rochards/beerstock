package com.rochards.beerstock.service;

import com.rochards.beerstock.builder.BeerDTOBuilder;
import com.rochards.beerstock.dto.BeerDTO;
import com.rochards.beerstock.entity.Beer;
import com.rochards.beerstock.exception.type.BeerAlreadyExistException;
import com.rochards.beerstock.mapper.BeerMapper;
import com.rochards.beerstock.repository.BeerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
public class BeerServiceTest {

    private static final long INVALID_BEER_ID = 1L;
    private BeerMapper beerMapper = BeerMapper.INSTANCE;

    @Mock
    private BeerRepository beerRepository;

    @InjectMocks
    private BeerService beerService;

    @Test
    public void whenBeerInformedThenItShouldBeCreated() {
        // given
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedSavedBeer = beerMapper.toModel(expectedBeerDTO);

        // when
        // simulando uma chamada para o banco e simulando uma resposta
        Mockito.when(beerRepository.findByName(expectedBeerDTO.getName())).thenReturn(Optional.empty());
        Mockito.when(beerRepository.save(expectedSavedBeer)).thenReturn(expectedSavedBeer);

        // then
        BeerDTO createdBeerDTO = beerService.create(expectedBeerDTO);

        // utilizando o hamcrest
        assertThat(createdBeerDTO, is(equalTo(expectedBeerDTO)));
        assertThat(createdBeerDTO.getQuantity(), is(greaterThan(2)));

        /* utilizando o Assertions do JUnit
        Assertions.assertEquals(expectedBeerDTO, createdBeerDTO);*/
    }

    @Test
    public void whenAnAlreadyExistentBeerInformedThenAnExceptionShouldBeThrown() {
        // given
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer duplicatedBeer = beerMapper.toModel(expectedBeerDTO);

        // when
        // mockando uma beer ja existent
        Mockito.when(beerRepository.findByName(expectedBeerDTO.getName())).thenReturn(Optional.of(duplicatedBeer));

        // then
        Assertions.assertThrows(BeerAlreadyExistException.class, () -> beerService.create(expectedBeerDTO));
    }
}