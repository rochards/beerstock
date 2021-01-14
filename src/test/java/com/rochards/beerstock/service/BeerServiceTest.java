package com.rochards.beerstock.service;

import com.rochards.beerstock.builder.BeerDTOBuilder;
import com.rochards.beerstock.dto.BeerDTO;
import com.rochards.beerstock.entity.Beer;
import com.rochards.beerstock.exception.type.BeerAlreadyExistException;
import com.rochards.beerstock.exception.type.BeerNotFoundException;
import com.rochards.beerstock.mapper.BeerMapper;
import com.rochards.beerstock.repository.BeerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
public class BeerServiceTest {

    private static final long INVALID_BEER_ID = 1L;
    private final BeerMapper beerMapper = BeerMapper.INSTANCE;

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

    @Test
    public void whenRegisteredBeerNameIsInformedThenShouldReturnABeer() {
        // given
        BeerDTO expectedFoundBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedFoundBeer = beerMapper.toModel(expectedFoundBeerDTO);

        // when
        Mockito.when(beerRepository.findByName(expectedFoundBeer.getName())).thenReturn(Optional.of(expectedFoundBeer));

        //then
        Optional<BeerDTO> foundBeerDTO = beerService.findByName(expectedFoundBeer.getName());

        assertThat(foundBeerDTO.get(), is(equalTo(expectedFoundBeerDTO)));
    }

    @Test
    public void whenNoRegisteredBeerNameIsInformedThenShouldReturnNothing() {
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        Mockito.when(beerRepository.findByName(beerDTO.getName())).thenReturn(Optional.empty());

        Optional<BeerDTO> foundBeerDTO = beerService.findByName(beerDTO.getName());

        assertThat(foundBeerDTO.isEmpty(), is(equalTo(true)));
    }

    @Test
    public void whenRegisteredBeerIdIsInformedThenShouldReturnABeer() {
        BeerDTO expectedFoundBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedFoundBeer = beerMapper.toModel(expectedFoundBeerDTO);

        Mockito.when(beerRepository.findById(expectedFoundBeer.getId())).thenReturn(Optional.of(expectedFoundBeer));

        Optional<BeerDTO> foundBeerDTO = beerService.findById(expectedFoundBeer.getId());

        assertThat(foundBeerDTO.get(), is(equalTo(expectedFoundBeerDTO)));
    }

    @Test
    public void whenNoRegisteredBeerIdIsInformedThenShouldReturnNothing() {
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        Mockito.when(beerRepository.findById(beerDTO.getId())).thenReturn(Optional.empty());

        Optional<BeerDTO> foundBeerDTO = beerService.findById(beerDTO.getId());

        assertThat(foundBeerDTO.isEmpty(), is(equalTo(true)));
    }

    @Test
    public void whenListBeerIsCalledThenReturnAListOfBeers() {
        BeerDTO expectedFoundBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedFoundBeer = beerMapper.toModel(expectedFoundBeerDTO);

        Mockito.when(beerRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundBeer));

        List<BeerDTO> foundBeersDTO =  beerService.listAll();

        assertThat(foundBeersDTO, is(not(empty())));
        assertThat(foundBeersDTO.get(0), is(equalTo(expectedFoundBeerDTO)));
    }

    @Test
    public void whenListBeerIsCalledThenReturnAnEmptyListOfBeers() {
        Mockito.when(beerRepository.findAll()).thenReturn(Collections.emptyList());

        List<BeerDTO> foundBeersDTO =  beerService.listAll();

        assertThat(foundBeersDTO, is(empty()));
    }

    @Test
    public void whenExclusionIsCalledWithRegisteredBeerIdThenItShouldBeDeleted() {
        BeerDTO expectedDeletedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedDeletedBeer = beerMapper.toModel(expectedDeletedBeerDTO);

        Mockito.when(beerRepository.existsById(expectedDeletedBeer.getId())).thenReturn(true);
        Mockito.doNothing().when(beerRepository).deleteById(expectedDeletedBeer.getId());

        beerService.delete(expectedDeletedBeerDTO.getId());

        // como beerService.delete retorna nada, preciso confirmar se os metodos abaixo foram chamados
        Mockito.verify(beerRepository, Mockito.times(1)).existsById(expectedDeletedBeer.getId());
        Mockito.verify(beerRepository, Mockito.times(1)).deleteById(expectedDeletedBeer.getId());
    }

    @Test
    public void whenExclusionIsCalledWithNoRegisteredBeerIdThenAnExceptionShouldBeThrown() {
        long noExistentId = 1L;

        Mockito.when(beerRepository.existsById(noExistentId)).thenReturn(false);

        Assertions.assertThrows(BeerNotFoundException.class, () -> beerService.delete(noExistentId));
    }

    @Test
    public void whenIncrementIsCalledThenIncrementBeerStock() {
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedBeer = beerMapper.toModel(expectedBeerDTO);

        Mockito.when(beerRepository.findById(expectedBeer.getId())).thenReturn(Optional.of(expectedBeer));
        Mockito.when(beerRepository.save(expectedBeer)).thenReturn(expectedBeer);

        int quantityToIncrement = 45;
        int expectedQuantityAfterIncrement = expectedBeerDTO.getQuantity() + quantityToIncrement;
        BeerDTO incrementedBeerDTO = beerService.increment(expectedBeerDTO.getId(), quantityToIncrement);

        assertThat(expectedQuantityAfterIncrement, equalTo(incrementedBeerDTO.getQuantity()));
        assertThat(expectedQuantityAfterIncrement, lessThan(expectedBeerDTO.getMax()));
    }
}
