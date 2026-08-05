package com.ada.pedido.resources;

import com.ada.pedido.repositories.entities.ClienteEntity;
import com.ada.pedido.repositories.entities.PedidoEntity;
import com.ada.pedido.repositories.entities.StatusPedido;
import com.ada.pedido.resources.dto.ItemPedidoRequestDTO;
import com.ada.pedido.resources.dto.PedidoRequestDTO;
import com.ada.pedido.services.PedidoService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoResourceTest {

    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private PedidoResource pedidoResource;

    private PedidoEntity pedido;
    private PedidoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new PedidoRequestDTO(List.of(new ItemPedidoRequestDTO(1L, 2)));

        ClienteEntity cliente = new ClienteEntity();
        cliente.setNome("João");

        pedido = new PedidoEntity();
        pedido.setId(100L);
        pedido.setCliente(cliente);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(StatusPedido.NOVO);
        pedido.setMensagemStatus("");
        pedido.setItens(Collections.emptyList());
    }

    @Test
    @DisplayName("POST /pedidos - Deve realizar pedido e retornar HTTP 201 Created")
    void deveRealizarPedidoComStatus201() {
        when(pedidoService.create(any(PedidoRequestDTO.class))).thenReturn(pedido);

        Response response = pedidoResource.realizarPedido(requestDTO);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
        verify(pedidoService).create(requestDTO);
    }

    @Test
    @DisplayName("GET /pedidos - Deve listar pedidos e retornar HTTP 200 OK")
    void deveListarPedidos() {
        when(pedidoService.listarTodos()).thenReturn(List.of(pedido));

        Response response = pedidoResource.listarPedidos();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
        verify(pedidoService).listarTodos();
    }
}
