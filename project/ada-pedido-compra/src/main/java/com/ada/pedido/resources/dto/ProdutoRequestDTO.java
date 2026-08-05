package com.ada.pedido.resources.dto;

import com.ada.pedido.repositories.entities.ProdutoEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProdutoRequestDTO(
        @Size(max = 50, message = "Descrição deve ter no máximo 50 caracteres")
        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        @PositiveOrZero(message = "Preço deve ser maior ou igual a zero")
        @NotNull(message = "Preço é obrigatório")
        BigDecimal preco,

        @PositiveOrZero(message = "Estoque deve ser maior ou igual a zero")
        @NotNull(message = "Estoque é obrigatório")
        Integer estoque
) {

    public ProdutoEntity criarEntidade() {
        var produto = new ProdutoEntity();
        produto.setDescricao(descricao);
        produto.setPreco(preco);
        produto.setEstoque(estoque);
        return produto;
    }

    public void copiarParaEntidade(ProdutoEntity produto) {
        produto.setDescricao(descricao);
        produto.setPreco(preco);
        produto.setEstoque(estoque);
    }

    public void copiarParaEntidadeNaoNulo(ProdutoEntity produto) {
        if (descricao != null) {
            produto.setDescricao(descricao);
        }
        if (preco != null) {
            produto.setPreco(preco);
        }
        if (estoque != null) {
            produto.setEstoque(estoque);
        }
    }

}
