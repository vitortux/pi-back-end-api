package br.com.codaedorme.pi.domain.usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	@Override
	Page<Usuario> findAll(Pageable pageable);

	Optional<Usuario> findByEmail(String email);
}