package br.com.avaliacao.backend.intermediario.exception;

public class CartaoInexistenteException extends RuntimeException {

    public CartaoInexistenteException(String message) {
        super(message);
    }    
}
