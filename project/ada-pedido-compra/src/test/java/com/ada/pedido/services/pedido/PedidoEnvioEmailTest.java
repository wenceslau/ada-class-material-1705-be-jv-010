package com.ada.pedido.services.pedido;

import com.ada.pedido.repositories.entities.ClienteEntity;
import com.ada.pedido.repositories.entities.ItemPedidoEntity;
import com.ada.pedido.repositories.entities.PedidoEntity;
import com.ada.pedido.repositories.entities.ProdutoEntity;
import com.ada.pedido.repositories.entities.StatusPedido;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PedidoEnvioEmailTest {

    @Test
    @DisplayName("Deve processar envio de e-mail sem lançar exceção")
    void deveProcessarEnvioEmailSemErros() {
        PedidoEnvioEmail envioEmail = new PedidoEnvioEmail();

        ClienteEntity cliente = new ClienteEntity();
        cliente.setNome("Maria Oliveira");
        cliente.setEmail("maria@email.com");

        ProdutoEntity produto = new ProdutoEntity();
        produto.setDescricao("Notebook");

        ItemPedidoEntity item = new ItemPedidoEntity();
        item.setProduto(produto);
        item.setQuantidade(1);

        PedidoEntity pedido = new PedidoEntity();
        pedido.setId(100L);
        pedido.setCliente(cliente);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(StatusPedido.PROCESSADO);
        pedido.setItens(List.of(item));

        assertDoesNotThrow(() -> envioEmail.processar(pedido));
    }
}
