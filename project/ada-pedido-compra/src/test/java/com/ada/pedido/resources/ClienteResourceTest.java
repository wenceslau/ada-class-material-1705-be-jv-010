package com.ada.pedido.resources;

import com.ada.pedido.repositories.entities.ClienteEntity;
import com.ada.pedido.repositories.entities.TipoUsuario;
import com.ada.pedido.resources.dto.ClienteRequestDTO;
import com.ada.pedido.services.ClienteService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteResourceTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteResource clienteResource;

    private ClienteEntity cliente;
    private ClienteRequestDTO dto;

    @BeforeEach
    void setUp() {
        dto = new ClienteRequestDTO("Carlos", "carlos@email.com", "senha12345");

        cliente = new ClienteEntity();
        cliente.setId(1L);
        cliente.setNome("Carlos");
        cliente.setEmail("carlos@email.com");
        cliente.setSenha("hash");
        cliente.setTipoUsuario(TipoUsuario.CLIENTE);
    }

    @Test
    @DisplayName("POST /clientes - Deve criar cliente e retornar HTTP 201 Created")
    void deveCriarClienteEComStatus201() {
        when(clienteService.criarCliente(any(ClienteRequestDTO.class))).thenReturn(cliente);

        Response response = clienteResource.criar(dto);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
        verify(clienteService).criarCliente(dto);
    }

    @Test
    @DisplayName("GET /clientes/{clienteId} - Deve retornar cliente e HTTP 200 OK")
    void deveBuscarClientePorId() {
        when(clienteService.buscarClientePorId(1L)).thenReturn(cliente);

        Response response = clienteResource.buscarPorId(1L);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("GET /clientes - Deve buscar todos os clientes e retornar HTTP 200 OK")
    void deveBuscarTodosClientes() {
        when(clienteService.buscarTodos()).thenReturn(List.of(cliente));

        Response response = clienteResource.buscarTodos();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(clienteService).buscarTodos();
    }

    @Test
    @DisplayName("PUT /clientes/{id} - Deve atualizar cliente e retornar HTTP 200 OK")
    void deveAtualizarCliente() {
        when(clienteService.atualizarCliente(eq(1L), any(ClienteRequestDTO.class))).thenReturn(cliente);

        Response response = clienteResource.atualizar(1L, dto);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(clienteService).atualizarCliente(1L, dto);
    }

    @Test
    @DisplayName("PATCH /clientes/{id} - Deve atualizar cliente parcialmente e retornar HTTP 200 OK")
    void deveAtualizarClienteParcialmente() {
        when(clienteService.atualizarClienteParcial(eq(1L), any(ClienteRequestDTO.class))).thenReturn(cliente);

        Response response = clienteResource.atualizacaoParcial(1L, dto);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(clienteService).atualizarClienteParcial(1L, dto);
    }

    @Test
    @DisplayName("DELETE /clientes/{id} - Deve deletar cliente e retornar HTTP 244 No Content")
    void deveDeletarCliente() {
        Response response = clienteResource.deletar(1L);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(clienteService).deletarCliente(1L);
    }
}
