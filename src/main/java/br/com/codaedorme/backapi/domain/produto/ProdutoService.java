package br.com.codaedorme.backapi.domain.produto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.codaedorme.backapi.domain.produto.enums.Status;

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

    @Transactional
    public Page<Produto> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public void alterarStatus(Produto produto) {
        produto.setStatus(produto.getStatus().equals(Status.ATIVO) ? Status.INATIVO : Status.ATIVO);
        repository.save(produto);
    }
}
