package com.ada.pedido.services.pedido;

import com.ada.pedido.repositories.entities.PedidoEntity;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;

@Priority(8)
@ApplicationScoped
public class PedidoSolicitarEntrega implements ProcessarPedido {

    @Override
    public void processar(PedidoEntity pedidoEntity) {

        // consumir uma fila no Kafka para solicitar ao meu sistema uma entrega

    }
}
