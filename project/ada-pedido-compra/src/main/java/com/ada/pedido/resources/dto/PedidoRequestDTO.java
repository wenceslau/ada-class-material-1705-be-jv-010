package com.ada.pedido.resources.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PedidoRequestDTO(
        @NotNull
        @NotEmpty(message = "O pedido deve ter pelo menos um item")
        List<ItemPedidoRequestDTO> itens) {
}
