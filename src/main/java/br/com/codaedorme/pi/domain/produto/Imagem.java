package br.com.codaedorme.pi.domain.produto;

import jakarta.persistence.*;

@Entity
@Table(name = "Imagem")
public class Imagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String nome;

    @Column(length = 255, nullable = false)
    private String diretorioDestino;

    @ManyToOne
    @JoinColumn(name = "produtoId", nullable = false)
    private Produto produto;

    @Column(nullable = false)
    private Boolean imagemPrincipal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDiretorioDestino() {
        return diretorioDestino;
    }

    public void setDiretorioDestino(String diretorioDestino) {
        this.diretorioDestino = diretorioDestino;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Boolean getImagemPrincipal() {
        return imagemPrincipal;
    }

    public void setImagemPrincipal(Boolean imagemPrincipal) {
        this.imagemPrincipal = imagemPrincipal;
    }

    @Override
    public String toString() {
        return "Imagem ID: " + id +
                " | Nome: " + nome +
                " | Diretorio Destino: " + diretorioDestino +
                " | Imagem Principal: " + imagemPrincipal;
    }
}
