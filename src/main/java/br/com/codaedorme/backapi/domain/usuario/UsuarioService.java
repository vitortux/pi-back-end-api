package br.com.codaedorme.backapi.domain.usuario;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.codaedorme.backapi.domain.usuario.enums.Status;
import br.com.codaedorme.backapi.infra.criptografia.CriptografaSenha;
import br.com.codaedorme.backapi.infra.validation.ValidaSenha;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private ValidaSenha validador;

	@Autowired
	private CriptografaSenha crip;

	public Usuario save(Usuario usuario, String senha2) {
		if (validador.validaSenhas(usuario.getSenha(), senha2)) {
			usuario.setSenha(crip.criptografar(usuario.getSenha()));
			return repository.save(usuario);
		} else {
			System.out.println("Senha nao compativeis!");
			return null;
		}
	}

	public Usuario alterarSenha(Usuario usuario, String senhaNova) {
		usuario.setSenha(crip.criptografar(senhaNova));
		return repository.save(usuario);
	}

	public Usuario findById(Long id) {
		return repository.findById(id).orElse(null);
	}

	public Page<Usuario> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Usuario alter(Usuario usuario) {
		return repository.save(usuario);
	}

	public void alterarStatus(Usuario usuario) {
		usuario.setStatus(usuario.getStatus().equals(Status.ATIVO) ? Status.INATIVO : Status.ATIVO);
		repository.save(usuario);
	}

	public Optional<Usuario> login(String email, String password) {
		Optional<Usuario> existingUser = repository.findByEmail(email);

		if (existingUser.isPresent() && validador.validaHash(password, existingUser.get().getId())) {
			return existingUser;
		}

		return Optional.empty();
	}

}
