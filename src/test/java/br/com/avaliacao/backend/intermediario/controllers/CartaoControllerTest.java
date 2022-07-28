package br.com.avaliacao.backend.intermediario.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.avaliacao.backend.intermediario.controllers.dto.CartaoDto;
import br.com.avaliacao.backend.intermediario.exception.CartaoJaExisteException;
import br.com.avaliacao.backend.intermediario.models.Cartao;
import br.com.avaliacao.backend.intermediario.services.CartaoService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CartaoControllerTest {

    @Autowired
	private MockMvc mockMvc;

    @MockBean
    private CartaoService cartaoService;

    @Autowired
    private ObjectMapper mapper;

    private Cartao testCard;

    @BeforeEach
    void setUp() {
        this.testCard = new Cartao(6549873025634501l, "1234", new BigDecimal("500"));
    }

    @Test
    void failToCreateCardWithInvalidData() throws Exception {
        var uri = new URI("/cartoes");

        var json = "{\"invalidKey\":" + "\"1234\", \"wrongKey\": \"1234\"}";

        mockMvc.perform(MockMvcRequestBuilders
            .post(uri)
            .content(json)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(
                status()
                .isUnprocessableEntity()
            )
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$", hasSize(3)));

            verify(cartaoService, times(0)).create(any(Cartao.class));
    }

    @Test
    void failWhenRegisterAnExistingCard() throws Exception {
        var uri = new URI("/cartoes");

        var json = mapper.writeValueAsString(new CartaoDto(this.testCard));

        when(cartaoService.create(any(Cartao.class))).thenThrow(new CartaoJaExisteException("Invalid Card", this.testCard));

        mockMvc.perform(MockMvcRequestBuilders
            .post(uri)
            .content(json)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(
                status()
                .isUnprocessableEntity()
            )
            .andExpect(jsonPath("$.numeroCartao", is(this.testCard.getNumero())))
            .andExpect(jsonPath("$.senha", is(this.testCard.getSenha())));
    }

    @Test
    void testCreateACardSuccessfully() throws Exception {
        URI uri = new URI("/cartoes");

        var json = mapper.writeValueAsString(new CartaoDto(this.testCard));

        when(cartaoService.create(any(Cartao.class))).thenReturn(this.testCard);

        mockMvc.perform(MockMvcRequestBuilders
            .post(uri)
            .content(json)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(
                status()
                .isCreated()
            )
            .andExpect(jsonPath("$.numeroCartao", is(this.testCard.getNumero())))
            .andExpect(jsonPath("$.senha", is(this.testCard.getSenha())));
    }

    @Test
    void getBalanceFromAnExistentCardSuccessfully() throws Exception {
        URI uri = new URI("/cartoes/" + this.testCard.getNumero());

        when(cartaoService.findByNumeroCartao(anyLong())).thenReturn(Optional.of(this.testCard));

        mockMvc.perform(MockMvcRequestBuilders
            .get(uri)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(
                status()
                .isOk()
            )
            .andExpect(content().string(this.testCard.getSaldo().toString()));
    }

    @Test
    void failWhenGetBalanceFromANonExistentCard() throws Exception {
        var uri = new URI("/cartoes/11");

        when(cartaoService.findByNumeroCartao(anyLong())).thenReturn(Optional.ofNullable(null));

        mockMvc.perform(MockMvcRequestBuilders
            .get(uri)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(
                status()
                .isNotFound()
            )
            .andExpect(content().string(""));
    }
}
