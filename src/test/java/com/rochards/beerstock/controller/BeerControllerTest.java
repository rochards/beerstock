package com.rochards.beerstock.controller;

import com.rochards.beerstock.builder.BeerDTOBuilder;
import com.rochards.beerstock.dto.BeerDTO;
import com.rochards.beerstock.dto.QuantityDTO;
import com.rochards.beerstock.exception.APIExceptionHandler;
import com.rochards.beerstock.exception.type.BeerNotFoundException;
import com.rochards.beerstock.exception.type.BeerStockExceededException;
import com.rochards.beerstock.service.BeerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;
import java.util.Optional;

import static com.rochards.beerstock.utils.JSONConversionUtils.asJSONString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BeerControllerTest {

    private static final String BEER_API_URL_PATH = "/api/v1/beers";
    private static final long VALID_BEER_ID = 1L;
    private static final long INVALID_BEER_ID = 2L;

    private MockMvc mockMvc;

    @Mock
    private BeerService beerService;

    @InjectMocks
    private BeerController beerController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(beerController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .setControllerAdvice(APIExceptionHandler.class) // importante informar as classes das excecoes, se
                // nao os testes que as retornam, nao funciona
                .build();
    }

    @Test
    public void whenPOSTIsCalledThenABeerIsCreated() throws Exception {
        // given
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        // when
        when(beerService.create(beerDTO)).thenReturn(beerDTO);

        // then
        mockMvc.perform(post(BEER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJSONString(beerDTO))) // asJSONString was created by me
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(beerDTO.getName())))
                .andExpect(jsonPath("$.brand", is(beerDTO.getBrand())))
                .andExpect(jsonPath("$.type", is(beerDTO.getType().toString())));
    }

    @Test
    public void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        beerDTO.setBrand(null);

        // then
        mockMvc.perform(post(BEER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJSONString(beerDTO))) // asJSONString was created by me
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenGETIsCalledWithRegisteredNameThenOkStatusIsReturned() throws Exception {
        // given
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        // when
        when(beerService.findByName(beerDTO.getName())).thenReturn(Optional.of(beerDTO));

        // then
        mockMvc.perform(get(BEER_API_URL_PATH + "/name/" + beerDTO.getName())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJSONString(beerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(beerDTO.getName())))
                .andExpect(jsonPath("$.brand", is(beerDTO.getBrand())))
                .andExpect(jsonPath("$.type", is(beerDTO.getType().toString())));
    }

    @Test
    public void whenGETIsCalledWithNoRegisteredNameThenNotFoundStatusIsReturned() throws Exception {
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        when(beerService.findByName(beerDTO.getName())).thenReturn(Optional.empty());

        mockMvc.perform(get(BEER_API_URL_PATH + "/name/" + beerDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenGETIsCalledWithRegisteredIdThenOkStatusIsReturned() throws Exception {
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        when(beerService.findById(beerDTO.getId())).thenReturn(Optional.of(beerDTO));

        mockMvc.perform(get(BEER_API_URL_PATH + "/" + beerDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJSONString(beerDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(asJSONString(beerDTO)));
    }

    @Test
    public void whenGETIsCalledWithNoRegisteredIdThenNotFoundStatusIsReturned() throws Exception {
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        when(beerService.findById(beerDTO.getId())).thenReturn(Optional.empty());

        mockMvc.perform(get(BEER_API_URL_PATH + "/" + beerDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenGETListOfBeersIsCalledThenOkStatusIsReturned() throws Exception {
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        when(beerService.listAll()).thenReturn(Collections.singletonList(beerDTO));

        mockMvc.perform(get(BEER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(beerDTO.getName())))
                .andExpect(jsonPath("$[0].brand", is(beerDTO.getBrand())))
                .andExpect(jsonPath("$[0].type", is(beerDTO.getType().toString())));
    }

    @Test
    public void whenDELETEIsCalledWithRegisteredIdThenNoContentStatusIsReturned() throws Exception {
        doNothing().when(beerService).delete(VALID_BEER_ID);

        mockMvc.perform(delete(BEER_API_URL_PATH + "/" + VALID_BEER_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void whenDELETEIsCalledWithNoRegisteredIdThenAnErrorIsReturned() throws Exception {
        doThrow(BeerNotFoundException.class).when(beerService).delete(INVALID_BEER_ID);

        mockMvc.perform(delete(BEER_API_URL_PATH + "/" + INVALID_BEER_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenPATCHIsCalledToIncrementThenOkStatusIsReturned() throws Exception {
        int quantityToIncrement = 10;
        QuantityDTO quantityDTO = new QuantityDTO(quantityToIncrement);
        BeerDTO incrementedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        incrementedBeerDTO.setQuantity(incrementedBeerDTO.getQuantity() + quantityToIncrement);

        when(beerService.incrementStock(incrementedBeerDTO.getId(), quantityToIncrement)).thenReturn(incrementedBeerDTO);

        mockMvc.perform(patch(BEER_API_URL_PATH + "/" + incrementedBeerDTO.getId() + "/increment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJSONString(quantityDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(asJSONString(incrementedBeerDTO)));
    }

    @Test
    public void whenPATCHIsCalledToDecrementAndFinalQuantityIsGreaterThanMaxThenBadRequestStatusIsReturned() throws Exception {
        int quantityToIncrement = 45;
        QuantityDTO quantityDTO = new QuantityDTO(quantityToIncrement);
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        beerDTO.setQuantity(beerDTO.getQuantity() + quantityToIncrement);

        doThrow(BeerStockExceededException.class).when(beerService).incrementStock(beerDTO.getId(),
                quantityToIncrement);

        mockMvc.perform(patch(BEER_API_URL_PATH + "/" + beerDTO.getId() + "/increment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJSONString(quantityDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenPATCHIsCalledToDecrementThenOkStatusIsReturned() throws Exception {
        int quantityToDecrement = 10;
        QuantityDTO quantityDTO = new QuantityDTO(quantityToDecrement);
        BeerDTO decrementedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        decrementedBeerDTO.setQuantity(decrementedBeerDTO.getQuantity() - quantityToDecrement);

        when(beerService.decrementStock(decrementedBeerDTO.getId(), quantityToDecrement)).thenReturn(decrementedBeerDTO);

        mockMvc.perform(patch(BEER_API_URL_PATH + "/" + decrementedBeerDTO.getId() + "/decrement")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJSONString(quantityDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(asJSONString(decrementedBeerDTO)));
    }

    @Test
    public void whenPATCHIsCalledToDecrementAndFinalQuantityIsLessThanZeroThenBadRequestStatusIsReturned() throws Exception {
        int quantityDoDecrement = 45;
        QuantityDTO quantityDTO = new QuantityDTO(quantityDoDecrement);
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        beerDTO.setQuantity(beerDTO.getQuantity() - quantityDoDecrement);

        doThrow(BeerStockExceededException.class).when(beerService).decrementStock(beerDTO.getId(),
                quantityDoDecrement);

        mockMvc.perform(patch(BEER_API_URL_PATH + "/" + beerDTO.getId() + "/decrement")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJSONString(quantityDTO)))
                .andExpect(status().isBadRequest());
    }
}
