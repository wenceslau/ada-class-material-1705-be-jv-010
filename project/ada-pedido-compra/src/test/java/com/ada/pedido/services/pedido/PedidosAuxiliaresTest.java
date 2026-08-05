package com.ada.pedido.services.pedido;

import com.ada.pedido.repositories.entities.PedidoEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PedidosAuxiliaresTest {

    @Test
    @DisplayName("Deve executar PedidoSolicitarEntrega sem lançar exceção")
    void deveExecutarSolicitarEntrega() {
        PedidoSolicitarEntrega solicitarEntrega = new PedidoSolicitarEntrega();
        assertDoesNotThrow(() -> solicitarEntrega.processar(new PedidoEntity()));
    }

    @Test
    @DisplayName("Deve executar PedidoCalculaBonus sem lançar exceção")
    void deveExecutarCalculaBonus() {
        PedidoCalculaBonus calculaBonus = new PedidoCalculaBonus();
        assertDoesNotThrow(() -> calculaBonus.processar(new PedidoEntity()));
    }
}
