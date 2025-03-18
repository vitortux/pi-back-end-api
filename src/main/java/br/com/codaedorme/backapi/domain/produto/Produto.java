package br.com.codaedorme.backapi.domain.produto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.codaedorme.backapi.domain.produto.enums.Status;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Produto")
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 200, nullable = false)
	private String nome;

	@Column(nullable = false, precision = 2, scale = 1)
	private BigDecimal avaliacao;

	@Column(length = 2000)
	private String descricao;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal preco;

	@Column(nullable = false)
	private Integer quantidadeEstoque;

	@OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<Imagem> imagens = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(BigDecimal avaliacao) {
		if (avaliacao.compareTo(BigDecimal.ZERO) < 0 || avaliacao.compareTo(BigDecimal.valueOf(5)) > 0) {
			throw new IllegalArgumentException("Avaliação deve ser entre 1 e 5.");
		}

		BigDecimal avaliacaoMultiplicada = avaliacao.multiply(BigDecimal.TEN);
		if (avaliacaoMultiplicada.remainder(BigDecimal.valueOf(5)).compareTo(BigDecimal.ZERO) != 0) {
			throw new IllegalArgumentException("Avaliação deve ser entre 1.0 e 5.0, com incrementos de 0.5");
		}
		this.avaliacao = avaliacao;
	}

	public Long getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public Integer getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(Integer quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public List<Imagem> getImagens() {
		return imagens;
	}

	public void setImagens(List<Imagem> imagens) {
		this.imagens = imagens;
	}

	public void addImagem(Imagem imagem) {
		this.imagens.add(imagem);
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Produto [id=" + id + ", nome=" + nome + ", quantidadeEstoque=" + quantidadeEstoque + ", preco=" + preco
				+ ", status=" + status + "]";
	}
}
