package br.com.avaliacao.backend.intermediario.models;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "cartoes")
public class Cartao {
    
    @Id
    private Long numero;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private BigDecimal saldo;

    @OneToMany(mappedBy = "cartao")
    private List<Transacao> transacoes;

    public Cartao() {
    }

    public Cartao(Long numero, String senha, BigDecimal saldo) {
        this.numero = numero;
        this.senha = senha;
        this.saldo = saldo;
    }

    public Long getNumero() {
        return numero;
    }

    public String getSenha() {
        return senha;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public boolean isValidPassword(String password) {
        return this.senha.equals(password);
    }

    public boolean hasBalance(BigDecimal value) {
        return saldo.compareTo(value) >= 0;
    }

    public void subtractBalanceValue(BigDecimal value) {
        this.saldo = this.saldo.subtract(value);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((numero == null) ? 0 : numero.hashCode());
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
        Cartao other = (Cartao) obj;
        if (numero == null) {
            if (other.numero != null)
                return false;
        } else if (!numero.equals(other.numero))
            return false;
        return true;
    }

    
}
