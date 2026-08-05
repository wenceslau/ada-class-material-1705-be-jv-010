package com.ada.pedido.resources.dto;

import com.ada.pedido.repositories.entities.PedidoEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(
        Long id,
        LocalDateTime data,
        String cliente,
        String status,
        String mensagemStatus,
        List<ItemPedidoResponseDTO> itens,
        BigDecimal totalPedido) {

    public static PedidoResponseDTO criarDeEntidade(PedidoEntity pedido) {
        if (pedido == null) {
            return null;
        }

        var totalPedido = BigDecimal.ZERO;

        for (var item : pedido.getItens()) {
            totalPedido = totalPedido.add(
                    item.getPreco().multiply(
                            BigDecimal.valueOf(item.getQuantidade())
                    )
            );
        }

        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getDataPedido(),
                pedido.getCliente().getNome(),
                pedido.getStatus().name(),
                pedido.getMensagemStatus(),
                pedido.getItens().stream()
                        .map(ItemPedidoResponseDTO::criarDeEntidade)
                        .toList(),
                totalPedido);

    }


}
