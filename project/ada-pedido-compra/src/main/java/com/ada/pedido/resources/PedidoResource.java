package com.ada.pedido.resources;

import com.ada.pedido.resources.dto.PedidoRequestDTO;
import com.ada.pedido.resources.dto.PedidoResponseDTO;
import com.ada.pedido.services.PedidoService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Authenticated
@Path("/pedidos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Pedidos", description = "Endpoints para criação e acompanhamento de pedidos de compra")
public class PedidoResource {

    private final PedidoService pedidoService;

    public PedidoResource(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @POST
    @RolesAllowed({"CLIENTE", "ADMIN"})
    @Operation(summary = "Realizar novo pedido", description = "Cria um novo pedido para o cliente autenticado, valida o estoque e processa a reserva dos itens.")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Pedido criado e enviado para processamento com sucesso"),
            @APIResponse(responseCode = "400", description = "Dados do pedido inválidos ou produto inexistente"),
            @APIResponse(responseCode = "401", description = "Não autenticado")
    })
    public Response realizarPedido(@Valid PedidoRequestDTO pedidoDTO) {

        var pedido = pedidoService.create(pedidoDTO);

        return Response
                .status(Response.Status.CREATED)
                .entity(PedidoResponseDTO.criarDeEntidade(pedido))
                .build();
    }

    @GET
    @RolesAllowed({"ADMIN"})
    @Operation(summary = "Listar todos os pedidos", description = "Retorna o histórico de todos os pedidos realizados no sistema. Requer perfil ADMIN.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso"),
            @APIResponse(responseCode = "401", description = "Não autenticado"),
            @APIResponse(responseCode = "403", description = "Acesso negado - Requer perfil ADMIN")
    })
    public Response listarPedidos() {

        var pedidos = pedidoService.listarTodos();

        var pedidosResponse = pedidos.stream()
                .map(PedidoResponseDTO::criarDeEntidade)
                .toList();

        return Response
                .status(Response.Status.OK)
                .entity(pedidosResponse)
                .build();
    }

}
