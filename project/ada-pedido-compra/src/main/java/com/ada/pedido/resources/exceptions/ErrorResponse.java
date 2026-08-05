package com.ada.pedido.resources.exceptions;

import java.time.LocalDateTime;

public record ErrorResponse(
        String exceptionClass,
        String mensagem,
        LocalDateTime ocorreuEm
) {
}
