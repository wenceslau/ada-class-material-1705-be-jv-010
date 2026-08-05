package com.ada.pedido.services.pedido;

import com.ada.pedido.repositories.PedidoRepository;
import com.ada.pedido.repositories.ProdutoRepository;
import com.ada.pedido.repositories.entities.ItemPedidoEntity;
import com.ada.pedido.repositories.entities.PedidoEntity;
import com.ada.pedido.repositories.entities.ProdutoEntity;
import com.ada.pedido.repositories.entities.StatusPedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoBaixarEstoqueTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoBaixarEstoque pedidoBaixarEstoque;

    private PedidoEntity pedido;
    private ProdutoEntity produto;
    private ItemPedidoEntity itemPedido;

    @BeforeEach
    void setUp() {
        produto = new ProdutoEntity();
        produto.setId(1L);
        produto.setDescricao("Mouse");
        produto.setEstoque(20);

        itemPedido = new ItemPedidoEntity();
        itemPedido.setProduto(produto);
        itemPedido.setQuantidade(3);

        pedido = new PedidoEntity();
        pedido.setId(10L);
        pedido.setStatus(StatusPedido.NOVO);
        pedido.setMensagemStatus("");
        pedido.setItens(List.of(itemPedido));
    }

    @Test
    @DisplayName("Deve baixar estoque e atualizar status para PROCESSADO se pedido for válido")
    void deveBaixarEstoqueEAtualizarStatus() {
        when(produtoRepository.findByIdOptional(1L)).thenReturn(Optional.of(produto));

        pedidoBaixarEstoque.processar(pedido);

        assertEquals(17, produto.getEstoque());
        assertEquals(StatusPedido.PROCESSADO, pedido.getStatus());
        verify(produtoRepository).persist(produto);
        verify(pedidoRepository).persist(pedido);
    }

    @Test
    @DisplayName("Não deve baixar estoque se status do pedido for NAO_PROCESSADO")
    void naoDeveBaixarEstoqueSeStatusNaoProcessado() {
        pedido.setStatus(StatusPedido.NAO_PROCESSADO);

        pedidoBaixarEstoque.processar(pedido);

        assertEquals(20, produto.getEstoque());
        assertEquals(StatusPedido.NAO_PROCESSADO, pedido.getStatus());
        verify(produtoRepository, never()).persist(any(ProdutoEntity.class));
        verify(pedidoRepository).persist(pedido);
    }
}
