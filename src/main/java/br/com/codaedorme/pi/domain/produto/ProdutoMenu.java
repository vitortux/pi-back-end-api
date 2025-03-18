package br.com.codaedorme.pi.domain.produto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.codaedorme.pi.domain.produto.enums.Status;
import br.com.codaedorme.pi.domain.usuario.Session;
import br.com.codaedorme.pi.domain.usuario.enums.Grupo;

@Component
public class ProdutoMenu {

	private static final Scanner SCANNER = new Scanner(System.in);

	@Autowired
	private ProdutoService service;

	@Autowired
	private Session session;

	public void listarProdutos() {
		System.out.println("------ Lista de Produtos ------");

		Produto[] produtos = service.findAll();
		if (isAdministrador()) {
			if (produtos.length == 0) {
				System.out.println("Nenhum produto cadastrado.");
			} else {
				for (int i = produtos.length - 1; i >= 0; i--) {
					System.out.println(produtos[i]);
				}
			}
			opcoesListar();
		} else {

			opcoesListarEstoquista();
			for (int i = produtos.length - 1; i >= 0; i--) {
				System.out.println(produtos[i]);
			}
		}

	}

	private void cadastrar() {
		if (!isAdministrador()) {
			System.out.println("Apenas ADMs podem cadastrar produtos.");
			return;
		}

		try {
			System.out.println("------ Cadastro ------\n");
			Produto produto = new Produto();

			System.out.println("Insira o nome do produto:");
			produto.setNome(SCANNER.nextLine());

			System.out.println("Insira a avaliação do produto (entre 1 e 5, com incrementos de 0.5):");
			produto.setAvaliacao(SCANNER.nextBigDecimal());
			SCANNER.nextLine();// eu te odeio scanner eu te odeio aaaaaaaaaaaaaa

			System.out.println("Insira a descrição do produto:");
			produto.setDescricao(SCANNER.nextLine());

			System.out.println("Insira o preço do produto:");
			produto.setPreco(SCANNER.nextBigDecimal());
			SCANNER.nextLine();// eu te odeio scanner eu te odeio aaaaaaaaaaaaaa

			System.out.println("Insira a quantidade em estoque do produto:");
			produto.setQuantidadeEstoque(SCANNER.nextInt());

			produto.setStatus(Status.ATIVO);

			Produto produtoSalvo = service.save(produto);

			do {
				Imagem imagem = cadastrarImagem(produtoSalvo);
				if (imagem != null) {
					imagem.setProduto(produtoSalvo);
					produtoSalvo.getImagens().add(imagem);
				}

				System.out.println("Deseja adicionar mais uma imagem? (S/N)");
			} while (SCANNER.nextLine().trim().equalsIgnoreCase("S"));

			service.save(produtoSalvo);
		} catch (IllegalArgumentException e) {
			System.out.println("Erro ao cadastrar o produto: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Erro inesperado: " + e.getMessage());
		}
	}

	private Imagem cadastrarImagem(Produto produtoSalvo) {
		Imagem imagem = new Imagem();

		SCANNER.nextLine();// eu te odeio scanner eu te odeio aaaaaaaaaaaaaa
		System.out.println("Digite o nome do arquivo da imagem:");
		String nomeArquivo = SCANNER.nextLine();
		imagem.setNome(nomeArquivo);

		System.out.println("Digite o caminho completo da imagem de origem:");
		Path origem = Path.of(SCANNER.nextLine());

		String extensao = "";
		String nomeOriginal = origem.getFileName().toString();
		int pontoIndex = nomeOriginal.lastIndexOf(".");
		if (pontoIndex != -1) {
			extensao = nomeOriginal.substring(pontoIndex);
		}

		Path destinoDiretorio = Path.of("src/main/resources/imagens/" + produtoSalvo.getId());
		try {
			Files.createDirectories(destinoDiretorio);

			Path destinoArquivo = destinoDiretorio.resolve(nomeArquivo + extensao);

			Files.copy(origem, destinoArquivo, StandardCopyOption.REPLACE_EXISTING);
			imagem.setDiretorioDestino(destinoArquivo.toString());

		} catch (IOException e) {
			System.out.println("Erro ao mover a imagem: " + e.getMessage());
			return null;
		}

		System.out.println("Esta é a imagem principal? (S/N)");
		imagem.setImagemPrincipal(SCANNER.nextLine().trim().equalsIgnoreCase("S"));

		if (imagem.getImagemPrincipal()) {
			resetarImagensPrincipais(produtoSalvo);
		}

		System.out.println("Imagem adicionada.");
		return imagem;
	}

	private void opcoesListar() {
		System.out.println("\nI - Incluir produto\nID - Editar/Ativar/Desativar produto\n0 - Voltar para o inicio");
		String opcao = SCANNER.nextLine();

		if (opcao.equalsIgnoreCase("i")) {
			cadastrar();
			return;
		}

		if (opcao.equals("0")) {
			System.out.println("Voltando ao menu...");
			return;
		}

		if (isNumeric(opcao)) {
			Long id = Long.parseLong(opcao);
			opcoesAlteracaoProduto(id);
			return;
		}

		System.out.println("Opção inválida.");
		opcoesListar();
	}

	private void opcoesListarEstoquista() {
		System.out.println("\nID - Editar/Ativar/Desativar produto\n0 - Voltar para o inicio");
		String opcao = SCANNER.nextLine();

		if (opcao.equals("0")) {
			System.out.println("Voltando ao menu...");
			return;
		}

		if (isNumeric(opcao)) {
			Long id = Long.parseLong(opcao);
			opcoesAlteracaoProduto(id);
			return;
		}

		System.out.println("Opção inválida.");
		opcoesListar();
	}

	private void opcoesAlteracaoProduto(Long id) {
		try {
			Produto produto = service.findById(id);
			System.out.println(produto.toString());

			System.out.println(
					"1 - Editar produto\n2 - Lista de imagens p/ alteração\n3 - Ativar ou desativar produto\n0 - Voltar para o inicio");

			int opcao = SCANNER.nextInt();
			SCANNER.nextLine();

			switch (opcao) {
			case 1:
				if (isAdministrador()) {
					editarProduto(produto);
				} else {
					editarQtdEstoque(produto);
				}
				break;
			case 2:
				if (isAdministrador()) {
					listarImagens(produto);
				} else {
					System.out.println("Apenas ADMs podem listar imagens.");
				}
				break;
			case 3:
				alterarStatus(produto);
				break;
			case 0:
				System.out.println("Voltando ao menu...");
				break;
			default:
				System.out.println("Opção inválida.");
				opcoesAlteracaoProduto(id);
			}
		} catch (NullPointerException e) {
			System.out.println("Produto não encontrado.");
		} catch (Exception e) {
			System.out.println("Erro inesperado: " + e.getMessage());
		}
	}

	private void listarImagens(Produto produtoAtt) {
		Produto produto = service.findById(produtoAtt.getId());
		if (produto == null) {
			System.out.println("Produto não encontrado.");
			return;
		}

		System.out.println("------ Lista de Imagens produto " + produto.getId() + " ------");
		for (Imagem img : produto.getImagens()) {
			System.out.println("ID: " + img.getId() + " - " + img.toString());
		}

		System.out.println("ID para remover\n0 para voltar\nI para incluir");
		String opcao = SCANNER.next();
		SCANNER.nextLine();

		// if (opcao.equalsIgnoreCase("i")) {
		// cadastrarImagem(addImagem);
		// return;
		// }

		if (opcao.equalsIgnoreCase("i")) {
			Imagem novaImagem = cadastrarImagem(produto);
			if (novaImagem != null) {
				novaImagem.setProduto(produto);
				produto.getImagens().add(novaImagem);
				service.save(produto); // Salvar as alterações no produto
				System.out.println("Imagem adicionada com sucesso!");
			}
			return;
		}

		if (opcao.equals("0")) {
			System.out.println("Voltando ao menu...");
			listarProdutos();
			return;
		}

		if (isNumeric(opcao)) {
			Long id = Long.parseLong(opcao);
			Imagem imagemParaRemover = null;
			for (Imagem img : produto.getImagens()) {
				if (img.getId().equals(id)) {
					imagemParaRemover = img;
					break;
				}
			}

			if (imagemParaRemover != null) {
				produto.getImagens().remove(imagemParaRemover);
				System.out.println("Imagem removida com sucesso!");
				service.save(produto); // Salvar as alterações no produto
			} else {
				System.out.println("Imagem não encontrada.");
			}
		} else {
			System.out.println("Opção inválida.");
		}

		listarImagens(produto); // Voltar a listar as imagens após a operação
	}

	private void editarQtdEstoque(Produto produto) {
		System.out.println("------ Edição ------\n");
		System.out.println(produto.toString());

		System.out.println("Insira a quantidade em estoque do produto:");
		produto.setQuantidadeEstoque(SCANNER.nextInt());
		SCANNER.nextLine();

		System.out.println("Deseja persistir as alterações? (S/N)");
		if (SCANNER.nextLine().trim().equalsIgnoreCase("S")) {
			service.save(produto);
		} else {
			System.out.println("Alterações descartadas.");
		}
	}

	private void editarProduto(Produto produto) {

		System.out.println("------ Edição ------\n");

		System.out.println("Insira o nome do produto:");
		produto.setNome(SCANNER.nextLine());

		System.out.println("Insira o preço do produto:");
		produto.setPreco(SCANNER.nextBigDecimal());
		SCANNER.nextLine();

		System.out.println("Insira a quantidade em estoque do produto:");
		produto.setQuantidadeEstoque(SCANNER.nextInt());
		SCANNER.nextLine();

		System.out.println("Insira a descrição do produto:");
		produto.setDescricao(SCANNER.nextLine());

		System.out.println("Insira a avaliação do produto (entre 1 e 5, com incrementos de 0.5):");
		produto.setAvaliacao(SCANNER.nextBigDecimal());
		SCANNER.nextLine();

		System.out.println("Deseja persistir as alterações? (S/N)");
		if (SCANNER.nextLine().trim().equalsIgnoreCase("S")) {
			service.save(produto);
		} else {
			System.out.println("Alterações descartadas.");
		}
	}

	private void alterarStatus(Produto produto) {
		try {
			System.out.println(produto.toString());

			String mensagem = produto.getStatus().equals(Status.ATIVO) ? "Deseja desativar o produto? (Y/N)"
					: "Deseja ativar o produto? (Y/N)";

			System.out.println(mensagem);
			String confirmacao = SCANNER.nextLine();

			if (confirmacao.equalsIgnoreCase("Y")) {
				service.alterarStatus(produto);
				System.out.println("Status alterado com sucesso!");
			} else if (!confirmacao.equalsIgnoreCase("N")) {
				System.out.println("Opção inválida!");
			} else {
				System.out.println("Status não alterado!");
				opcoesAlteracaoProduto(produto.getId());
			}
		} catch (NullPointerException e) {
			System.out.println("Usuario não encontrado!");
		} catch (Exception e) {
			System.out.println("Erro inesperado: " + e.getMessage());
		}
	}

	private void resetarImagensPrincipais(Produto produto) {
		for (Imagem img : produto.getImagens()) {
			img.setImagemPrincipal(false);
		}
	}

	private boolean isAdministrador() {
		return session.getUsuario().getGrupo() == Grupo.ADMINISTRADOR;
	}

	public static boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public void setSession(Session session) {
		this.session = session;
	}
}
