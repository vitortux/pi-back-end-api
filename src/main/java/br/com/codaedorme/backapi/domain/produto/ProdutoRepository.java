package br.com.codaedorme.backapi.domain.produto;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

	// @EntityGraph(attributePaths = "imagens")
	Optional<Produto> findById(Long id);

	// @EntityGraph(attributePaths = "imagens")
	Page<Produto> findAll(Pageable pageable);
}
