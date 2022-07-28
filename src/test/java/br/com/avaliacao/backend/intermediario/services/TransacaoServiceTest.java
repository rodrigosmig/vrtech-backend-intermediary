package br.com.avaliacao.backend.intermediario.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.avaliacao.backend.intermediario.controllers.forms.NovaTransacaoForm;
import br.com.avaliacao.backend.intermediario.exception.InvalidTransactionData;
import br.com.avaliacao.backend.intermediario.exception.SaldoInsuficienteException;
import br.com.avaliacao.backend.intermediario.models.Cartao;
import br.com.avaliacao.backend.intermediario.models.Transacao;
import br.com.avaliacao.backend.intermediario.repositories.TransacaoRepository;

@SpringBootTest
class TransacaoServiceTest {

    @InjectMocks
    private TransacaoService transacaoService;

    @Mock
    private CartaoService cartaoService;

    @Mock
    private TransacaoRepository transacaoRepository;

    private Cartao testCard;
    private Transacao testTransaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        this.testCard = new Cartao(6549873025634501l, "1234", new BigDecimal("500"));
        this.testTransaction = new Transacao(1l, new BigDecimal("100"), LocalDateTime.now(), this.testCard);
    }
    
    @Test
    void createATransactionSuccessfully() {
        var form = new NovaTransacaoForm(6549873025634501l, "1234", new BigDecimal("100"));

        when(cartaoService.findByNumeroCartao(anyLong()))
            .thenReturn(Optional.of(this.testCard));

        when(transacaoRepository.save(any(Transacao.class)))
            .thenReturn(this.testTransaction);

        var newTransaction = transacaoService.create(form);

        verify(cartaoService).subtractBalanceValue(any(Cartao.class), any(BigDecimal.class));
        verify(transacaoRepository).save(any(Transacao.class));
        assertEquals(this.testTransaction, newTransaction);
    }

    @Test
    void failWhenThePasswordIsInvalid() {
        var form = new NovaTransacaoForm(6549873025634501l, "123456", new BigDecimal("100"));

        when(cartaoService.findByNumeroCartao(anyLong()))
            .thenReturn(Optional.of(this.testCard));

        var exception = assertThrows(InvalidTransactionData.class, () -> transacaoService.create(form));

        verify(cartaoService, times(0)).subtractBalanceValue(any(Cartao.class), any(BigDecimal.class));
        verify(transacaoRepository, times(0)).save(any(Transacao.class));
        assertEquals("SENHA_INVALIDA", exception.getMessage());
    }

    @Test
    void failWhenTheBalanceIsInsufficient() {
        var form = new NovaTransacaoForm(6549873025634501l, "1234", new BigDecimal("600"));

        when(cartaoService.findByNumeroCartao(anyLong()))
            .thenReturn(Optional.of(this.testCard));

        var exception = assertThrows(SaldoInsuficienteException.class, () -> transacaoService.create(form));

        verify(cartaoService, times(0)).subtractBalanceValue(any(Cartao.class), any(BigDecimal.class));
        verify(transacaoRepository, times(0)).save(any(Transacao.class));
        assertEquals("SALDO_INSUFICIENTE", exception.getMessage());
    }

    @Test
    void failWhenTheCardNumerIsInvalid() {
        var form = new NovaTransacaoForm(123456789123l, "1234", new BigDecimal("100"));

        when(cartaoService.findByNumeroCartao(anyLong()))
            .thenReturn(Optional.ofNullable(null));

        var exception = assertThrows(InvalidTransactionData.class, () -> transacaoService.create(form));

        verify(cartaoService, times(0)).subtractBalanceValue(any(Cartao.class), any(BigDecimal.class));
        verify(transacaoRepository, times(0)).save(any(Transacao.class));
        assertEquals("CARTAO_INEXISTENTE", exception.getMessage());
    }
}
