package com.ada.pedido.services;

import com.ada.pedido.repositories.ClienteRepository;
import com.ada.pedido.repositories.PedidoRepository;
import com.ada.pedido.repositories.ProdutoRepository;
import com.ada.pedido.repositories.entities.ItemPedidoEntity;
import com.ada.pedido.repositories.entities.PedidoEntity;
import com.ada.pedido.repositories.entities.StatusPedido;
import com.ada.pedido.resources.dto.ItemPedidoRequestDTO;
import com.ada.pedido.resources.dto.PedidoRequestDTO;
import com.ada.pedido.services.pedido.PedidoException;
import com.ada.pedido.services.pedido.ProcessarPedido;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class PedidoService {

    private final Instance<ProcessarPedido> listaProcessarPedido;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;
    private final PedidoRepository pedidoRepository;
    private final SecurityIdentity securityIdentity;


    public PedidoService(Instance<ProcessarPedido> listaProcessarPedido,
                         ProdutoRepository produtoRepository,
                         ClienteRepository clienteRepository,
                         PedidoRepository pedidoRepository,
                         SecurityIdentity securityIdentity) {

        this.listaProcessarPedido = listaProcessarPedido;
        this.produtoRepository = produtoRepository;
        this.clienteRepository = clienteRepository;
        this.pedidoRepository = pedidoRepository;
        this.securityIdentity = securityIdentity;
    }

    @Transactional
    public PedidoEntity create(PedidoRequestDTO pedidoDTO) {

        var clienteEmail = securityIdentity.getPrincipal().getName();
        var cliente = clienteRepository
                .buscarPorEmail(clienteEmail)
                .orElseThrow(() -> new PedidoException("Usuario não encontrado!"));

        var pedido = new PedidoEntity();
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(StatusPedido.NOVO);
        pedido.setMensagemStatus("");
        pedido.setCliente(cliente);

        var itens = pedidoDTO.itens().stream()
                .map(itemPedidoRequest -> construirItemPedido(itemPedidoRequest, pedido))
                .toList();

        pedido.setItens(itens);

        for (ProcessarPedido processarPedido : listaProcessarPedido) {
            processarPedido.processar(pedido);
        }

        /* X-POINT
            não lançar exceção para pedido não processado.
            a exceção gera rollback, portanto é necessário manter no banco todos os pedidos.

            if (pedido.getStatus().equals(StatusPedido.NAO_PROCESSADO)) {
                throw new PedidoException("Pedido não pode ser realizado! " + pedido.getMensagemStatus());
            }
        */

        return pedido;

    }

    private ItemPedidoEntity construirItemPedido(ItemPedidoRequestDTO itemPedidoDTO, PedidoEntity pedido) {

        var produto = produtoRepository
                .findByIdOptional(itemPedidoDTO.produtoId())
                .orElseThrow(() -> new PedidoException("Produto não encontrado! Id: " + itemPedidoDTO.produtoId()));

        var itemPedido = new ItemPedidoEntity();
        itemPedido.setProduto(produto);
        itemPedido.setQuantidade(itemPedidoDTO.quantidade());
        itemPedido.setPedido(pedido);
        itemPedido.setPreco(produto.getPreco());

        return itemPedido;
    }

    public List<PedidoEntity> listarTodos() {
        var list = this.pedidoRepository.listAll();
        return list;
    }
}
