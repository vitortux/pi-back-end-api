package br.com.codaedorme.backapi.domain.usuario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.codaedorme.backapi.domain.usuario.Usuario;
import br.com.codaedorme.backapi.domain.usuario.UsuarioService;
import br.com.codaedorme.backapi.infra.validation.ValidaSenha;
import br.com.codaedorme.pi.domain.usuario.enums.Status;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService service;

	@Autowired
	private ValidaSenha validador;

	@GetMapping
	public ResponseEntity<List<Usuario>> listarUsuarios() {
		List<Usuario> usuarios = service.findAll();
		if (usuarios.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(usuarios);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Usuario> listarUsuarioPorId(@PathVariable Long id) {
		Usuario usuario = service.findById(id);
		if (usuario == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(usuario);
	}

//	@PostMapping
//	public ResponseEntity<String> cadastrar(@RequestBody Usuario usuario, @RequestParam String senha2) {
//		try {
//			if (usuario.getGrupo() != Grupo.ADMINISTRADOR) {
//				return ResponseEntity.status(403).body("Apenas ADMs podem cadastrar usuários.");
//			}
//			service.save(usuario, senha2);
//			return ResponseEntity.status(201).body("Usuário cadastrado com sucesso!");
//		} catch (DataIntegrityViolationException e) {
//			return ResponseEntity.badRequest().body("Email já cadastrado no banco de dados!");
//		} catch (IllegalArgumentException e) {
//			return ResponseEntity.badRequest().body("Digite um dado válido!");
//		} catch (Exception e) {
//			return ResponseEntity.status(500).body("Erro inesperado: " + e.getMessage());
//		}
//	}

	@PutMapping("/{id}")
	public ResponseEntity<String> alterarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
		try {
			Usuario usuarioExistente = service.findById(id);
			if (usuarioExistente == null) {
				return ResponseEntity.notFound().build();
			}
			usuarioExistente.setNome(usuario.getNome());
			usuarioExistente.setCpf(usuario.getCpf());
			usuarioExistente.setGrupo(usuario.getGrupo());

			service.alter(usuarioExistente);
			return ResponseEntity.ok("Alterações salvas com sucesso!");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Erro inesperado: " + e.getMessage());
		}
	}

	@PatchMapping("/{id}/senha")
	public ResponseEntity<String> alterarSenha(@PathVariable Long id, @RequestParam String senhaNova,
			@RequestParam String senhaNova2) {
		Usuario usuario = service.findById(id);
		if (usuario == null) {
			return ResponseEntity.notFound().build();
		}

		if (!validador.validaSenhas(senhaNova, senhaNova2)) {
			return ResponseEntity.badRequest().body("Senhas não compatíveis!");
		}

		service.alterarSenha(usuario, senhaNova);
		return ResponseEntity.ok("Senha alterada com sucesso!");
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<String> alterarStatus(@PathVariable Long id) {
		Usuario usuario = service.findById(id);
		if (usuario == null) {
			return ResponseEntity.notFound().build();
		}

		String mensagem = usuario.getStatus().equals(Status.ATIVO) ? "Deseja desativar o usuário? (Y/N)"
				: "Deseja ativar o usuário? (Y/N)";

		// Aqui você pode personalizar mais, mas a lógica seria similar à original
		service.alterarStatus(usuario);
		return ResponseEntity.ok("Status alterado com sucesso!");
	}
}
