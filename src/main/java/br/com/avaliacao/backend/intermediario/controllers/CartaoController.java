package br.com.avaliacao.backend.intermediario.controllers;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.avaliacao.backend.intermediario.controllers.dto.CartaoDto;
import br.com.avaliacao.backend.intermediario.controllers.forms.NovoCartaoForm;
import br.com.avaliacao.backend.intermediario.exception.CartaoInexistenteException;
import br.com.avaliacao.backend.intermediario.models.Cartao;
import br.com.avaliacao.backend.intermediario.services.CartaoService;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {
    
    @Autowired
    private CartaoService cartaoService;

    @PostMapping
    @Transactional
    public ResponseEntity<CartaoDto> create(@RequestBody @Valid NovoCartaoForm form, UriComponentsBuilder uriBuilder) {
        var cartao = cartaoService.create(form.convert());

        URI uri = uriBuilder.path("/cartoes/{numeroCartao}").buildAndExpand(cartao.getNumero()).toUri();
        
        return ResponseEntity.created(uri).body(new CartaoDto(cartao));
    }

    @GetMapping("/{numeroCartao}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Long numeroCartao) {
        Optional<Cartao> optional = cartaoService.findByNumeroCartao(numeroCartao);

        var cartao = optional.orElseThrow(() -> new CartaoInexistenteException("Cartão não existe"));

        return ResponseEntity.ok(cartao.getSaldo());
    }
}
