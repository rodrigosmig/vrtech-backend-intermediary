package br.com.avaliacao.backend.intermediario.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import br.com.avaliacao.backend.intermediario.exception.CartaoJaExisteException;
import br.com.avaliacao.backend.intermediario.models.Cartao;
import br.com.avaliacao.backend.intermediario.repositories.CartaoRepository;

@SpringBootTest
class CartaoServiceTest {

    @InjectMocks
    private CartaoService cartaoService;

    @MockBean
    private CartaoRepository cartaoRepository;

    private Cartao testCard;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        this.testCard = new Cartao(6549873025634501l, "1234", new BigDecimal("500"));
    }

    @Test
    void createCardSuccessfully() {
        when(cartaoRepository.save(any(Cartao.class)))
            .thenReturn(this.testCard);
        
        var newCard = cartaoService.create(new Cartao());
        
        verify(cartaoRepository).save(any(Cartao.class));
        assertNotNull(newCard);
        assertEquals(Cartao.class, newCard.getClass());
        assertEquals(this.testCard.getNumero(), newCard.getNumero());
    }

    @Test
    void failToCreateAnExistingCard() {
        var message = "Cartão já cadastrado";
        
        when(cartaoRepository.existsByNumero(anyLong()))
            .thenReturn(true);
        
        var exception = assertThrows(CartaoJaExisteException.class, () -> cartaoService.create(this.testCard));
        
        assertEquals(message, exception.getMessage());
    }

    @Test
    void findACardSuccessfully() {
        when(cartaoRepository.findById(anyLong()))
            .thenReturn(Optional.of(this.testCard));
        
        Optional<Cartao> optional = cartaoService.findByNumeroCartao(1l);

        assertTrue(optional.isPresent());
        assertEquals(this.testCard, optional.get());
        verify(cartaoRepository).findById(anyLong());
    }

    @Test
    void findAnInvalidCard() {
        when(cartaoRepository.findById(anyLong()))
            .thenReturn(Optional.ofNullable(null));
        
        Optional<Cartao> optional = cartaoService.findByNumeroCartao(1l);

        assertFalse(optional.isPresent());
        verify(cartaoRepository).findById(anyLong());
    }

    @Test
    void debitACardValueSuccessfully() {
        when(cartaoRepository.save(any(Cartao.class)))
            .thenReturn(this.testCard);

        var value = new BigDecimal("100");

        var card = cartaoService.subtractBalanceValue(this.testCard, value);

        assertEquals(new BigDecimal("400"), card.getSaldo());
    }
}
