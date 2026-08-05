package com.ada.pedido.services.pedido;

import com.ada.pedido.repositories.PedidoRepository;
import com.ada.pedido.repositories.ProdutoRepository;
import com.ada.pedido.repositories.entities.ItemPedidoEntity;
import com.ada.pedido.repositories.entities.PedidoEntity;
import com.ada.pedido.repositories.entities.ProdutoEntity;
import com.ada.pedido.repositories.entities.StatusPedido;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@Priority(20)
@ApplicationScoped
public class PedidoBaixarEstoque implements ProcessarPedido {

    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;

    public PedidoBaixarEstoque(ProdutoRepository produtoRepository, PedidoRepository pedidoRepository) {
        this.produtoRepository = produtoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public void processar(PedidoEntity pedido) {
        System.out.println("Etapa Baixar estoque");

        if (!StatusPedido.NAO_PROCESSADO.equals(pedido.getStatus())) {
            // Atualiza o estoque dos produtos do pedido
            pedido.getItens().forEach(this::atualizarEstoque);
            pedido.setStatus(StatusPedido.PROCESSADO);
        }

        pedidoRepository.persist(pedido);
        System.out.println(pedido.getId() + " - " + pedido.getStatus() + " - " + pedido.getMensagemStatus());

    }

    private void atualizarEstoque(ItemPedidoEntity itemPedido) {
        // Localiza o produto no banco de dados
        Optional<ProdutoEntity> byId = produtoRepository.findByIdOptional(itemPedido.getProduto().getId());

        // Verifica se o produto existe e atualiza o estoque
        if (byId.isPresent()) {
            ProdutoEntity produto = byId.get();
            produto.setEstoque(produto.getEstoque() - itemPedido.getQuantidade());
            produtoRepository.persist(produto);
        }
    }
}
