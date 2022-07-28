package br.com.avaliacao.backend.intermediario.services;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.avaliacao.backend.intermediario.exception.CartaoJaExisteException;
import br.com.avaliacao.backend.intermediario.models.Cartao;
import br.com.avaliacao.backend.intermediario.repositories.CartaoRepository;

@Service
public class CartaoService {

    @Autowired
    private CartaoRepository cartaoRepository;

	public Cartao create(Cartao cartao) {
        var exists = cartaoRepository.existsByNumero(cartao.getNumero());

        if (exists) {
            throw new CartaoJaExisteException("Cartão já cadastrado", cartao);
        }
        
        return cartaoRepository.save(cartao);
	}

    public Optional<Cartao> findByNumeroCartao(Long numeroCartao) {
        return cartaoRepository.findById(numeroCartao);
    }

    public Cartao subtractBalanceValue(Cartao cartao, BigDecimal value) {
        cartao.subtractBalanceValue(value);

        return cartaoRepository.save(cartao);
    }
}
