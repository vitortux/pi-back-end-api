package br.com.codaedorme.pi.domain.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.codaedorme.pi.domain.usuario.enums.Status;
import br.com.codaedorme.pi.infra.criptografia.CriptografaSenha;
import br.com.codaedorme.pi.infra.validation.ValidaSenha;

import java.util.Optional;

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

	public Usuario[] findAll() {
		return repository.findAll().toArray(new Usuario[0]);
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
