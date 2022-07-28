package br.com.avaliacao.backend.intermediario.controllers.forms;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import br.com.avaliacao.backend.intermediario.models.Cartao;

public class NovoCartaoForm {

    @NumberFormat(style = Style.NUMBER)
    @NotNull(message = "O campo numeroCartao é obrigatório")
    private Long numeroCartao;

    @NotNull(message = "O campo senha é obrigatório")
    @NotBlank(message = "O campo senha não pode ser vazio")
    @Length(min = 4, max = 8)
    private String senha;

    public Cartao convert() {
        return new Cartao(numeroCartao, senha, new BigDecimal("500"));
    }

    public Long getNumeroCartao() {
        return numeroCartao;
    }

    public String getSenha() {
        return senha;
    }
}
