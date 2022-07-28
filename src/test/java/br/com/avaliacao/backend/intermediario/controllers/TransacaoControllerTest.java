package br.com.avaliacao.backend.intermediario.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.net.URI;

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

import br.com.avaliacao.backend.intermediario.controllers.forms.NovaTransacaoForm;
import br.com.avaliacao.backend.intermediario.exception.InvalidTransactionData;
import br.com.avaliacao.backend.intermediario.exception.SaldoInsuficienteException;
import br.com.avaliacao.backend.intermediario.services.TransacaoService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TransacaoControllerTest {

    @Autowired
	private MockMvc mockMvc;

    @MockBean
    private TransacaoService cartaoService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void createTransactionSuccessfully() throws Exception {
        var uri = new URI("/transacoes");

        var form = new NovaTransacaoForm(1l, "1234", new BigDecimal("100"));

        var json = mapper.writeValueAsString(form);

        mockMvc.perform(MockMvcRequestBuilders
            .post(uri)
            .content(json)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(
                status()
                .isCreated()
            )
            .andExpect(content().string("OK"));
    }

    @Test
    void failWhenTheTransactionDataIsInvalid() throws Exception {
        var uri = new URI("/transacoes");

        var form = new NovaTransacaoForm(1l, "1234", new BigDecimal("100"));

        var json = mapper.writeValueAsString(form);

        var message = "CARTAO_INEXISTENTE";

        when(cartaoService.create(any(NovaTransacaoForm.class)))
            .thenThrow(new InvalidTransactionData(message));

        mockMvc.perform(MockMvcRequestBuilders
            .post(uri)
            .content(json)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(
                status()
                .isUnprocessableEntity()
            )
            .andExpect(content().string(message));
    }

    @Test
    void failWhenTheBalanceIsInsufficient() throws Exception {
        var uri = new URI("/transacoes");

        var form = new NovaTransacaoForm(1l, "1234", new BigDecimal("600"));

        var json = mapper.writeValueAsString(form);

        var message = "SALDO_INSUFICIENTE";

        when(cartaoService.create(any(NovaTransacaoForm.class)))
            .thenThrow(new SaldoInsuficienteException(message));

        mockMvc.perform(MockMvcRequestBuilders
            .post(uri)
            .content(json)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(
                status()
                .isUnprocessableEntity()
            )
            .andExpect(content().string(message));
    }
}
