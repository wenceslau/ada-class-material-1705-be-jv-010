package com.ada.pedido.resources;

import com.ada.pedido.repositories.ClienteEntity;
import com.ada.pedido.repositories.ClienteRepository;
import com.ada.pedido.resources.dto.ClienteDTO;
import com.ada.pedido.resources.exceptions.BusinessException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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

    private final ClienteRepository clienteRepository;

    public ClienteResource(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @POST
    @Path("/criar")
    @Transactional
    public Response criarCliente(@Valid ClienteDTO cliente) {

        if (cliente.nome().length() < 5){
            throw new BusinessException("Nome do cliente não pode ter menos de 5 caracteres");
        }

        var entity = cliente.criarEntity();
        clienteRepository.persist(entity);

        return Response
                .status(Response.Status.CREATED)
                .entity(ClienteDTO.fromEntity(entity))
                .build();

    }

    @GET
    @Path("/listar")
    public Response listarClientes() {

        var panacheQuery = clienteRepository.findAll();
        var listaClientes = panacheQuery.list();

//        var listaClientesDTO = listaClientes.stream()
//                .map(ClienteDTO::fromEntity)
//                .toList();

        var listaClientesDTO = new ArrayList<>();
        for (ClienteEntity cliente : listaClientes) {
            listaClientesDTO.add(ClienteDTO.fromEntity(cliente));
        }

        return Response
                .status(Response.Status.OK)
                .entity(listaClientesDTO)
                .build();
    }

    @GET
    @Path("/buscar/{id}")
    public Response buscarClientePorId(@PathParam("id") Long id) {

        var clienteEntity = clienteRepository.findById(id);

        if (clienteEntity == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("{\"message\": \"Cliente não encontrado\"}")
                    .build();
        }

        return Response
                .status(Response.Status.OK)
                .entity(ClienteDTO.fromEntity(clienteEntity))
                .build();

    }

    @DELETE
    @Path("/deletar/{id}")
    @Transactional
    public Response deletarCliente(@PathParam("id") Long id) {

        clienteRepository.deleteById(id);

        //var cliente = clienteRepository.findById(id);

//        if (!deletado) {
//            return Response
//                    .status(Response.Status.NOT_FOUND)
//                    .entity("{\"message\": \"Cliente não encontrado\"}")
//                    .build();
//        }

        //clienteRepository.delete(cliente);

        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }

    @PUT
    @Path("/atualizar/{id}")
    @Transactional
    public Response atualizarCliente(@PathParam("id") Long id, @Valid ClienteDTO clienteAtualizado) {
        var clienteEntity = clienteRepository.findById(id);

        if (clienteEntity == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("{\"message\": \"Cliente não encontrado\"}")
                    .build();
        }

        clienteEntity.setNome(clienteAtualizado.nome());
        clienteEntity.setEmail(clienteAtualizado.email());

        clienteRepository.persist(clienteEntity);

        return Response
                .status(Response.Status.OK)
                .entity(ClienteDTO.fromEntity(clienteEntity))
                .build();
    }

    @PATCH
    @Path("/atualizar-parcialmente/{id}")
    @Transactional
    public Response atualizarClienteParcialmente(@PathParam("id") Long id, ClienteDTO clienteAtualizado) {
        var clienteEntity = clienteRepository.findById(id);
        if (clienteEntity == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("{\"message\": \"Cliente não encontrado\"}")
                    .build();
        }

        if (clienteAtualizado.nome() != null) {
            clienteEntity.setNome(clienteAtualizado.nome());
        }
        if (clienteAtualizado.email() != null) {
            clienteEntity.setEmail(clienteAtualizado.email());
        }

        clienteRepository.persist(clienteEntity);

        return Response
                .status(Response.Status.OK)
                .entity(ClienteDTO.fromEntity(clienteEntity))
                .build();
    }

    @GET
    @Path("/buscar-por-email/{email}")
    public Response buscarClientePorEmail(@PathParam("email") String email) {
        var cliente = clienteRepository.findByEmail(email);

        if (cliente.isEmpty()) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("{\"message\": \"Cliente não encontrado\"}")
                    .build();
        }

        var clienteDTO = ClienteDTO.fromEntity(cliente.get());

        return Response
                .status(Response.Status.OK)
                .entity(clienteDTO)
                .build();
    }

    @GET
    @Path("/buscar-por-nome/{nome}")
    public Response buscarClientePorNome(@PathParam("nome") String nome) {
        var panacheQuery = clienteRepository.findByNameLike(nome);
        var listaClientes = panacheQuery.list();

        var listaClientesDTO = listaClientes.stream()
                .map(ClienteDTO::fromEntity)
                .toList();

        return Response
                .status(Response.Status.OK)
                .entity(listaClientesDTO)
                .build();
    }
}
