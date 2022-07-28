package br.com.avaliacao.backend.intermediario.exception;

import br.com.avaliacao.backend.intermediario.models.Cartao;

public class CartaoJaExisteException extends RuntimeException {
  
    private transient Cartao cartao;

    public CartaoJaExisteException(String message, Cartao cartao) {
        super(message);
        this.cartao = cartao;
    }

    public Cartao getCartao() {
        return cartao;
    }
}
