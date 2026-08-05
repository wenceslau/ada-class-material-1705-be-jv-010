package com.ada.pedido.services;

import com.ada.pedido.repositories.ProdutoRepository;
import com.ada.pedido.repositories.entities.ProdutoEntity;
import com.ada.pedido.resources.dto.ProdutoRequestDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public ProdutoEntity criarProduto(ProdutoRequestDTO produtoRequestDTO) {

        var produto = produtoRequestDTO.criarEntidade();
        validarProduto(produto);
        produtoRepository.persist(produto);
        return produto;

    }

    @Transactional
    public ProdutoEntity atualizarProduto(Long id, ProdutoRequestDTO produtoRequestDTO) {

        var produto = buscarProdutoPorId(id);
        produtoRequestDTO.copiarParaEntidade(produto);

        validarProduto(produto);
        produtoRepository.persist(produto);

        return produto;

    }

    @Transactional
    public ProdutoEntity atualizarProdutoParcial(Long id, ProdutoRequestDTO produtoRequestDTO) {

        var produto = buscarProdutoPorId(id);
        produtoRequestDTO.copiarParaEntidadeNaoNulo(produto);

        validarProduto(produto);
        produtoRepository.persist(produto);

        return produto;

    }

    public ProdutoEntity buscarProdutoPorId(Long id) {
        return produtoRepository.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado!"));
    }

    public List<ProdutoEntity> buscarProdutoPorDescricao(String filtro){
        return produtoRepository.findByDescricaoLikeIgnoreCase(filtro);
    }

    public List<ProdutoEntity> buscarTodos() {
        return produtoRepository.listAll();
    }

    private static void validarProduto(ProdutoEntity produto) {
        if (produto.getDescricao() == null || produto.getDescricao().isEmpty()){
            throw new IllegalArgumentException("Descrição não pode ser vazia!");
        }
        if (produto.getPreco() == null || produto.getPreco().compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Preço deve ser maior que zero!");
        }
        if (produto.getEstoque() == null || produto.getEstoque() < 0){
            throw new IllegalArgumentException("Estoque deve ser maior ou igual a zero!");
        }
    }

}
