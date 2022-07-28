package br.com.avaliacao.backend.intermediario.controllers.forms;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import br.com.avaliacao.backend.intermediario.models.Cartao;
import br.com.avaliacao.backend.intermediario.models.Transacao;

public class NovaTransacaoForm {

    @Digits(fraction = 0, integer = 16)
    @NotNull(message = "O campo numeroCartao é obrigatório")
    private Long numeroCartao;

    @NotNull(message = "O campo senha é obrigatório")
    @NotBlank(message = "O campo senha não pode ser vazio")
    @Length(min = 4, max = 8)
    private String senhaCartao;

    private BigDecimal valor;

    public NovaTransacaoForm() {
    }

    public NovaTransacaoForm(Long numeroCartao, String senhaCartao, BigDecimal valor) {
        this.numeroCartao = numeroCartao;
        this.senhaCartao = senhaCartao;
        this.valor = valor;
    }

    public Transacao convert(Cartao cartao) {
        return new Transacao(valor, LocalDateTime.now(), cartao);
    }
    
    public Long getNumeroCartao() {
        return numeroCartao;
    }
    public String getSenhaCartao() {
        return senhaCartao;
    }
    public BigDecimal getValor() {
        return valor;
    }
}
