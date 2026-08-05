package com.ada.pedido.resources.dto;

import com.ada.pedido.repositories.entities.ProdutoEntity;

import java.math.BigDecimal;

public record ProdutoResponseDTO(
        Long id,
        String descricao,
        BigDecimal preco,
        Integer estoque
) {

    public static ProdutoResponseDTO criarDeEntidade(ProdutoEntity produto) {
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getEstoque()
        );
    }

}
