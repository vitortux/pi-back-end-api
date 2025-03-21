package br.com.codaedorme.backapi.domain.produto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.codaedorme.backapi.domain.produto.Produto;
import br.com.codaedorme.backapi.domain.produto.ProdutoService;
import br.com.codaedorme.backapi.domain.produto.enums.Status;
import br.com.codaedorme.backapi.domain.usuario.Session;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

	@Autowired
	private ProdutoService service;

	@Autowired
	private Session session;

	/*
	 * Exemplo = /api/produtos?page=0&size=10&sortBy=id&sortDir=ASC
	 */
	@GetMapping
	public ResponseEntity<Page<Produto>> listarProdutos(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "ASC") String sortDir) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy);
		Page<Produto> produtos = service.findAll(pageable);

		if (produtos.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(produtos);
	}

	// Endpoint para cadastrar um produto
	// @PostMapping
	// public ResponseEntity<Produto> cadastrarProduto(@RequestBody Produto produto)
	// {
	// if (!isAdministrador()) {
	// return ResponseEntity.status(403).body(null); // Somente ADMs podem cadastrar
	// produtos
	// }
	//
	// try {
	// produto.setStatus(Status.ATIVO);
	// Produto produtoSalvo = service.save(produto);
	//
	// // Aqui você poderia chamar a lógica de imagem também se necessário
	//
	// return ResponseEntity.status(201).body(produtoSalvo);
	// } catch (Exception e) {
	// return ResponseEntity.status(500).body(null);
	// }
	// }

	// Endpoint para editar um produto
	@PutMapping("/{id}")

	public ResponseEntity<Produto> editarProduto(@PathVariable Long id, @RequestBody Produto produto) {
		try {
			Produto produtoExistente = service.findById(id);
			if (produtoExistente == null) {
				return ResponseEntity.notFound().build();
			}

			produtoExistente.setNome(produto.getNome());
			produtoExistente.setPreco(produto.getPreco());
			produtoExistente.setQuantidadeEstoque(produto.getQuantidadeEstoque());
			produtoExistente.setDescricao(produto.getDescricao());
			produtoExistente.setAvaliacao(produto.getAvaliacao());

			Produto produtoAtualizado = service.save(produtoExistente);
			return ResponseEntity.ok(produtoAtualizado);
		} catch (Exception e) {
			return ResponseEntity.status(500).build();
		}
	}

	// Endpoint para alterar o status de um produto
	@PatchMapping("/{id}/status")
	public ResponseEntity<Void> alterarStatus(@PathVariable Long id) {
		try {
			Produto produto = service.findById(id);
			if (produto == null) {
				return ResponseEntity.notFound().build();
			}

			Status novoStatus = produto.getStatus().equals(Status.ATIVO) ? Status.INATIVO : Status.ATIVO;
			produto.setStatus(novoStatus);
			service.save(produto);

			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.status(500).build();
		}
	}

	// Método auxiliar para verificar se o usuário é administrador
	// private boolean isAdministrador() {
	// return session.getUsuario().getGrupo() == Grupo.ADMINISTRADOR;
	// }
}
