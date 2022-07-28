package br.com.avaliacao.backend.intermediario.controllers;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.avaliacao.backend.intermediario.controllers.forms.NovaTransacaoForm;
import br.com.avaliacao.backend.intermediario.services.TransacaoService;

@RestController
@RequestMapping("transacoes")
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;
    
    @PostMapping
    @Transactional
    public ResponseEntity<String> createTransaction(@RequestBody @Valid NovaTransacaoForm form) {
        transacaoService.create(form);

        return ResponseEntity.status(HttpStatus.CREATED).body("OK");
    }
}
