package br.com.avaliacao.backend.intermediario.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "transacoes")
public class Transacao {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @ManyToOne(targetEntity = Cartao.class)
    @JoinColumn(name = "numero_cartao", nullable = false)
    private Cartao cartao;

    public Transacao() {
    }

    public Transacao(Long id, BigDecimal valor, LocalDateTime dataHora, Cartao cartao) {
        this.id = id;
        this.valor = valor;
        this.dataHora = dataHora;
        this.cartao = cartao;
    }

    public Transacao(BigDecimal valor, LocalDateTime dataHora, Cartao cartao) {
        this.valor = valor;
        this.dataHora = dataHora;
        this.cartao = cartao;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public Cartao getCartao() {
        return cartao;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cartao == null) ? 0 : cartao.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Transacao other = (Transacao) obj;
        if (cartao == null) {
            if (other.cartao != null)
                return false;
        } else if (!cartao.equals(other.cartao))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    
}
