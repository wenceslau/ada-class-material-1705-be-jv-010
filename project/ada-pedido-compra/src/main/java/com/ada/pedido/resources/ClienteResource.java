package com.ada.pedido.resources;

import com.ada.pedido.resources.dto.ClienteRequestDTO;
import com.ada.pedido.resources.dto.ClienteResponseDTO;
import com.ada.pedido.services.ClienteService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Authenticated
@Path("/clientes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Clientes", description = "Endpoints para gerenciamento de clientes")
public class ClienteResource {

    private final ClienteService clienteService;

    public ClienteResource(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @POST
    @PermitAll
    @Operation(summary = "Cadastrar novo cliente", description = "Endpoint público para cadastro de novo cliente no sistema. Todo cadastro público cria um usuário do tipo CLIENTE.")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Cliente cadastrado com sucesso"),
            @APIResponse(responseCode = "400", description = "Dados de entrada inválidos")
    })
    public Response criar(@Valid ClienteRequestDTO clienteDTO) {

        var clienteCriado = clienteService.criarCliente(clienteDTO);

        return Response
                .status(Response.Status.CREATED)
                .entity(ClienteResponseDTO.criarDeEntidade(clienteCriado))
                .build();
    }

    @GET
    @RolesAllowed("ADMIN")
    @Path("/{clienteId}")
    @Operation(summary = "Buscar cliente por ID", description = "Recupera os detalhes de um cliente através do seu ID. Requer perfil ADMIN.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Cliente encontrado"),
            @APIResponse(responseCode = "401", description = "Não autenticado"),
            @APIResponse(responseCode = "403", description = "Acesso negado - Requer perfil ADMIN"),
            @APIResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public Response buscarPorId(@Parameter(description = "ID do cliente", required = true) @PathParam("clienteId") Long id) {

        var cliente = clienteService.buscarClientePorId(id);

        return Response
                .ok(ClienteResponseDTO.criarDeEntidade(cliente))
                .build();
    }

    @GET
    @RolesAllowed({"ADMIN"})
    @Operation(summary = "Listar todos os clientes", description = "Retorna uma lista com todos os clientes cadastrados no sistema. Requer perfil ADMIN.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso"),
            @APIResponse(responseCode = "401", description = "Não autenticado"),
            @APIResponse(responseCode = "403", description = "Acesso negado - Requer perfil ADMIN")
    })
    public Response buscarTodos() {

        var listaClientes = clienteService.buscarTodos();

        var listaDTO = listaClientes
                .stream()
                .map(ClienteResponseDTO::criarDeEntidade)
                .toList();

        return Response
                .ok(listaDTO)
                .build();
    }

    @PUT
    @RolesAllowed("ADMIN")
    @Path("/{id}")
    @Operation(summary = "Atualizar cliente por completo", description = "Atualiza todos os dados de um cliente existente. Requer perfil ADMIN.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @APIResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @APIResponse(responseCode = "401", description = "Não autenticado"),
            @APIResponse(responseCode = "403", description = "Acesso negado - Requer perfil ADMIN"),
            @APIResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public Response atualizar(@Parameter(description = "ID do cliente", required = true) @PathParam("id") Long id, @Valid ClienteRequestDTO clienteDTO) {

        var cliente = clienteService.atualizarCliente(id, clienteDTO);

        return Response
                .ok(ClienteResponseDTO.criarDeEntidade(cliente))
                .build();
    }

    @PATCH
    @RolesAllowed("ADMIN")
    @Path("/{id}")
    @Operation(summary = "Atualizar cliente parcialmente", description = "Atualiza apenas os campos enviados no JSON de um cliente. Requer perfil ADMIN.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Cliente atualizado parcialmente com sucesso"),
            @APIResponse(responseCode = "401", description = "Não autenticado"),
            @APIResponse(responseCode = "403", description = "Acesso negado - Requer perfil ADMIN"),
            @APIResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public Response atualizacaoParcial(@Parameter(description = "ID do cliente", required = true) @PathParam("id") Long id, ClienteRequestDTO clienteDTO) {

        var cliente = clienteService.atualizarClienteParcial(id, clienteDTO);

        return Response
                .ok(ClienteResponseDTO.criarDeEntidade(cliente))
                .build();
    }

    @DELETE
    @RolesAllowed("ADMIN")
    @Path("/{id}")
    @Operation(summary = "Remover cliente", description = "Remove um cliente do sistema pelo seu ID. Requer perfil ADMIN.")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Cliente removido com sucesso"),
            @APIResponse(responseCode = "401", description = "Não autenticado"),
            @APIResponse(responseCode = "403", description = "Acesso negado - Requer perfil ADMIN")
    })
    public Response deletar(@Parameter(description = "ID do cliente a ser removido", required = true) @PathParam("id") Long id) {

        clienteService.deletarCliente(id);

        return Response
                .noContent()
                .build();
    }

}
