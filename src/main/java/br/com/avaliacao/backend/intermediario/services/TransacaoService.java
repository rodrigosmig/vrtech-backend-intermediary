package br.com.avaliacao.backend.intermediario.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.avaliacao.backend.intermediario.controllers.forms.NovaTransacaoForm;
import br.com.avaliacao.backend.intermediario.exception.InvalidTransactionData;
import br.com.avaliacao.backend.intermediario.exception.SaldoInsuficienteException;
import br.com.avaliacao.backend.intermediario.models.Cartao;
import br.com.avaliacao.backend.intermediario.models.Transacao;
import br.com.avaliacao.backend.intermediario.repositories.TransacaoRepository;

@Service
public class TransacaoService {
    
    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private CartaoService cartaoService;

    public Transacao create(NovaTransacaoForm form) {
        var cartao = authorize(form);

        cartaoService.subtractBalanceValue(cartao, form.getValor());

        return transacaoRepository.save(form.convert(cartao));
    }

    private Cartao authorize(NovaTransacaoForm form) {
        Optional<Cartao> cartaoOptional = cartaoService.findByNumeroCartao(form.getNumeroCartao());

        var cartao = cartaoOptional.orElseThrow(() -> new InvalidTransactionData("CARTAO_INEXISTENTE"));

        if (!cartao.isValidPassword(form.getSenhaCartao())) {
            throw new InvalidTransactionData("SENHA_INVALIDA");
        }

        if (!cartao.hasBalance(form.getValor())) {
            throw new SaldoInsuficienteException("SALDO_INSUFICIENTE");
        }

        return cartao;
    }
}
