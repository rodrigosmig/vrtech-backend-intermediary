package br.com.avaliacao.backend.intermediario.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.avaliacao.backend.intermediario.controllers.dto.CartaoDto;

@RestControllerAdvice
public class ApplicationExceptionHandler {
    
    @ExceptionHandler(CartaoJaExisteException.class)
    public ResponseEntity<CartaoDto> handleCartaoJaExisteException(CartaoJaExisteException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new CartaoDto(ex.getCartao()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<StandardError>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var errors = new ArrayList<StandardError>();
        var fieldErrors = ex.getBindingResult().getFieldErrors();

        fieldErrors.forEach(e -> {
            var error = new StandardError(e.getField(), e.getDefaultMessage());
            errors.add(error);
        });

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors);
    }

    @ExceptionHandler(CartaoInexistenteException.class)
    public ResponseEntity<String> handleCartaoNaoExisteException(CartaoInexistenteException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(InvalidTransactionData.class)
    public ResponseEntity<String> handleInvalidTransactionData(InvalidTransactionData ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getMessage());
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<String> handleSaldoInsuficienteException(SaldoInsuficienteException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getMessage());
    }
}
