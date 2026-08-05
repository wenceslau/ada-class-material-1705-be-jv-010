package com.ada.pedido.resources.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemPedidoRequestDTO(
        @NotNull
        Long produtoId,

        @NotNull
        @Positive
        Integer quantidade) {

}
