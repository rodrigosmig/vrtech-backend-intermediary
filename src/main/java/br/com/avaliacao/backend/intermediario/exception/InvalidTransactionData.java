package br.com.avaliacao.backend.intermediario.exception;

public class InvalidTransactionData extends RuntimeException {

    public InvalidTransactionData(String message) {
        super(message);
    }    
}
