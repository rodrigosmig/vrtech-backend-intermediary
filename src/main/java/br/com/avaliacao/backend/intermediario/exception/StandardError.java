package br.com.avaliacao.backend.intermediario.exception;

public class StandardError {
    private String field;
    private String message;

    public StandardError(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }
    
    public String getMessage() {
        return message;
    }

}
