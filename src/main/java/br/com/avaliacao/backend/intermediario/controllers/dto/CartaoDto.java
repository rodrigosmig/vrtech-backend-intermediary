package br.com.avaliacao.backend.intermediario.controllers.dto;

import br.com.avaliacao.backend.intermediario.models.Cartao;

public class CartaoDto {
    private Long numeroCartao;
    private String senha;

    public CartaoDto(Cartao cartao) {
        this.numeroCartao = cartao.getNumero();
        this.senha = cartao.getSenha();
    }

    public Long getNumeroCartao() {
        return numeroCartao;
    }

    public String getSenha() {
        return senha;
    }
}
