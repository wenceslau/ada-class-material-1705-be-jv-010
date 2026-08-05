package com.ada.pedido.resources.dto;


import com.ada.pedido.repositories.entities.ItemPedidoEntity;

import java.math.BigDecimal;

public record ItemPedidoResponseDTO(
        String produtoDescricao,
        BigDecimal produtoPreco,
        Integer quantidade,
        BigDecimal totalItem) {

    public static ItemPedidoResponseDTO criarDeEntidade(ItemPedidoEntity itemPedido){
        return new ItemPedidoResponseDTO(
                itemPedido.getProduto().getDescricao(),
                itemPedido.getProduto().getPreco(),
                itemPedido.getQuantidade(),
                itemPedido.getPreco().multiply(BigDecimal.valueOf(itemPedido.getQuantidade()))
        );
    }

}





