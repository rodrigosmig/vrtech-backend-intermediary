package br.com.avaliacao.backend.intermediario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.avaliacao.backend.intermediario.models.Cartao;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, Long> {
    
    boolean existsByNumero(Long id);
}
