package com.ada.pedido.services.pedido;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoValidarEstoqueTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private PedidoValidarEstoque pedidoValidarEstoque;

    private PedidoEntity pedido;
    private ProdutoEntity produto;
    private ItemPedidoEntity itemPedido;

    @BeforeEach
    void setUp() {
        produto = new ProdutoEntity();
        produto.setId(1L);
        produto.setDescricao("Teclado Mecânico");
        produto.setEstoque(10);

        itemPedido = new ItemPedidoEntity();
        itemPedido.setProduto(produto);
        itemPedido.setQuantidade(5);

        pedido = new PedidoEntity();
        pedido.setStatus(StatusPedido.NOVO);
        pedido.setItens(List.of(itemPedido));
    }

    @Test
    @DisplayName("Deve manter status do pedido quando estoque for suficiente")
    void deveValidarEstoqueComSucesso() {
        when(produtoRepository.findByIdOptional(1L)).thenReturn(Optional.of(produto));

        pedidoValidarEstoque.processar(pedido);

        assertEquals(StatusPedido.NOVO, pedido.getStatus());
        assertTrue(pedido.getMensagemStatus() == null || pedido.getMensagemStatus().isEmpty());
    }

    @Test
    @DisplayName("Deve alterar status para NAO_PROCESSADO quando estoque for insuficiente")
    void deveAlterarStatusQuandoEstoqueInsuficiente() {
        itemPedido.setQuantidade(15); // Maior que o estoque de 10
        when(produtoRepository.findByIdOptional(1L)).thenReturn(Optional.of(produto));

        pedidoValidarEstoque.processar(pedido);

        assertEquals(StatusPedido.NAO_PROCESSADO, pedido.getStatus());
        assertTrue(pedido.getMensagemStatus().contains("Estoque Produto Teclado Mecânico não disponível"));
    }

    @Test
    @DisplayName("Deve lançar PedidoException quando produto não for encontrado")
    void deveLancarExcecaoQuandoProdutoNaoEncontrado() {
        when(produtoRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        PedidoException exception = assertThrows(
                PedidoException.class,
                () -> pedidoValidarEstoque.processar(pedido)
        );

        assertTrue(exception.getMessage().contains("Produto 1 não encontrado"));
    }
}
