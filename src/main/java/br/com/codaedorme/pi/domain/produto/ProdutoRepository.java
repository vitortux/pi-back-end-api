package br.com.codaedorme.pi.domain.produto;

import br.com.codaedorme.pi.domain.usuario.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Override
    Page<Produto> findAll(Pageable pageable);
}
