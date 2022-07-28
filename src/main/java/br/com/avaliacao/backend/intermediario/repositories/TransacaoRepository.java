package br.com.avaliacao.backend.intermediario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.avaliacao.backend.intermediario.models.Transacao;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    
}
