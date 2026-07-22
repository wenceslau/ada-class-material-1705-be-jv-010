package com.ada.pedido.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/clientes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClienteResource {

    private final List<Cliente> clientes = new ArrayList<>();

    @POST
    @Path("/criar")
    public Response criarCliente(Cliente cliente) {

        cliente.setId(UUID.randomUUID().toString()); // Atribui um ID random para o cliente
        clientes.add(cliente);

        return Response
                .status(Response.Status.CREATED)
                .entity(cliente)
                .build();
    }

    @GET
    @Path("/listar")
    public Response listarClientes() {
        return Response
                .status(Response.Status.OK)
                .entity(clientes)
                .build();
    }

    @GET
    @Path("/buscar/{id}")
    public Response buscarClientePorId(@PathParam("id") String id){

        var cliente = clientes.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (cliente == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("{\"message\": \"Cliente não encontrado\"}")
                    .build();
        }

        return Response
                .status(Response.Status.OK)
                .entity(cliente)
                .build();

    }

    @DELETE
    @Path("/deletar/{id}")
    public Response deletarCliente(@PathParam("id") String id) {

        var cliente = clientes.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (cliente == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("{\"message\": \"Cliente não encontrado\"}")
                    .build();
        }

        clientes.remove(cliente);

        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }

    @PUT
    @Path("/atualizar/{id}")
    public Response atualizarCliente(@PathParam("id") String id, Cliente clienteAtualizado){
        var cliente = clientes.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (cliente == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("{\"message\": \"Cliente não encontrado\"}")
                    .build();
        }

        cliente.setNome(clienteAtualizado.getNome());
        cliente.setEmail(clienteAtualizado.getEmail());

        return Response
                .status(Response.Status.OK)
                .entity(cliente)
                .build();
    }

    @PATCH
    @Path("/atualizar-parcialmente/{id}")
    public Response atualizarClienteParcialmente(@PathParam("id") String id, Cliente clienteAtualizado){
        var cliente = clientes.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (cliente == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("{\"message\": \"Cliente não encontrado\"}")
                    .build();
        }

        if(clienteAtualizado.getNome() != null){
            cliente.setNome(clienteAtualizado.getNome());
        }
        if (clienteAtualizado.getEmail() != null){
            cliente.setEmail(clienteAtualizado.getEmail());
        }

        return Response
                .status(Response.Status.OK)
                .entity(cliente)
                .build();
    }

}
