package com.ada.pedido.services.pedido;

import com.ada.pedido.repositories.ProdutoRepository;
import com.ada.pedido.repositories.entities.ItemPedidoEntity;
import com.ada.pedido.repositories.entities.PedidoEntity;
import com.ada.pedido.repositories.entities.ProdutoEntity;
import com.ada.pedido.repositories.entities.StatusPedido;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Optional;

@Priority(30)
@ApplicationScoped
public class PedidoValidarEstoque implements ProcessarPedido {

    private final ProdutoRepository produtoRepository;

    public PedidoValidarEstoque(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Override
    @Transactional
    public void processar(PedidoEntity pedido) {
        System.out.println("Etapa Validar estoque");

        pedido.getItens().forEach(itemPedido -> validarEstoque(pedido, itemPedido));

    }

    private void validarEstoque(PedidoEntity pedido, ItemPedidoEntity itemPedido) {
        Optional<ProdutoEntity> byId = produtoRepository.findByIdOptional(itemPedido.getProduto().getId());

        if (byId.isEmpty()) {
            throw new PedidoException("Produto %s não encontrado".formatted(itemPedido.getProduto().getId()));
        }

        ProdutoEntity produto = byId.get();
        if (produto.getEstoque() < itemPedido.getQuantidade()) {
            String mensagem = "Estoque Produto %s não disponível. Pedido não processado!".formatted(produto.getDescricao());
            pedido.setStatus(StatusPedido.NAO_PROCESSADO);
            pedido.setMensagemStatus(mensagem);
        }

    }

}
