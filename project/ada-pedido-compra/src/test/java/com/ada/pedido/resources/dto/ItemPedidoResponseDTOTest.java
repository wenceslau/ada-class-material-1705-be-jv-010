package com.ada.pedido.resources.dto;

import com.ada.pedido.repositories.entities.ItemPedidoEntity;
import com.ada.pedido.repositories.entities.ProdutoEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ItemPedidoResponseDTOTest {

    @Test
    @DisplayName("Deve criar ItemPedidoResponseDTO a partir de ItemPedidoEntity")
    void deveCriarDeEntidade() {
        ProdutoEntity produto = new ProdutoEntity();
        produto.setDescricao("Mouse");
        produto.setPreco(new BigDecimal("50.00"));

        ItemPedidoEntity item = new ItemPedidoEntity();
        item.setProduto(produto);
        item.setPreco(new BigDecimal("50.00"));
        item.setQuantidade(3);

        ItemPedidoResponseDTO dto = ItemPedidoResponseDTO.criarDeEntidade(item);

        assertNotNull(dto);
        assertEquals("Mouse", dto.produtoDescricao());
        assertEquals(new BigDecimal("50.00"), dto.produtoPreco());
        assertEquals(3, dto.quantidade());
        assertEquals(new BigDecimal("150.00"), dto.totalItem());
    }
}
