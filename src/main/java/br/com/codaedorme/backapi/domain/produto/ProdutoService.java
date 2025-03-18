package br.com.codaedorme.backapi.domain.produto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.codaedorme.backapi.domain.produto.enums.Status;
import jakarta.transaction.Transactional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    public Produto save(Produto produto) {
        return repository.save(produto);
    }

    public Produto findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Produto[] findAll() {
        return repository.findAll().toArray(new Produto[0]);
    }

    public void alterarStatus(Produto produto) {
        produto.setStatus(produto.getStatus().equals(Status.ATIVO) ? Status.INATIVO : Status.ATIVO);
        repository.save(produto);
    }
}
