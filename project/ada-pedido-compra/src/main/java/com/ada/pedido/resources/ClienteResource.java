package com.ada.pedido.resources;

import com.ada.pedido.repositories.ClienteEntity;
import com.ada.pedido.repositories.ClienteRepository;
import jakarta.transaction.Transactional;
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
    public Response criarCliente(ClienteEntity clienteEntity) {

        clienteRepository.persist(clienteEntity);

        return Response
                .status(Response.Status.CREATED)
                .entity(clienteEntity)
                .build();
    }

    @GET
    @Path("/listar")
    public Response listarClientes() {

        var panacheQuery = clienteRepository.findAll();
        var listaClientes = panacheQuery.list();

        return Response
                .status(Response.Status.OK)
                .entity(listaClientes)
                .build();
    }

    @GET
    @Path("/buscar/{id}")
    public Response buscarClientePorId(@PathParam("id") Long id){

        var cliente = clienteRepository.findById(id);

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
    public Response atualizarCliente(@PathParam("id") Long id, ClienteEntity clienteEntityAtualizado){
        var cliente = clienteRepository.findById(id);

        if (cliente == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("{\"message\": \"Cliente não encontrado\"}")
                    .build();
        }

        cliente.setNome(clienteEntityAtualizado.getNome());
        cliente.setEmail(clienteEntityAtualizado.getEmail());

        clienteRepository.persist(cliente);

        return Response
                .status(Response.Status.OK)
                .entity(cliente)
                .build();
    }

    @PATCH
    @Path("/atualizar-parcialmente/{id}")
    @Transactional
    public Response atualizarClienteParcialmente(@PathParam("id") Long id, ClienteEntity clienteEntityAtualizado){
        var cliente = clienteRepository.findById(id);
        if (cliente == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("{\"message\": \"Cliente não encontrado\"}")
                    .build();
        }

        if(clienteEntityAtualizado.getNome() != null){
            cliente.setNome(clienteEntityAtualizado.getNome());
        }
        if (clienteEntityAtualizado.getEmail() != null){
            cliente.setEmail(clienteEntityAtualizado.getEmail());
        }

        clienteRepository.persist(cliente);

        return Response
                .status(Response.Status.OK)
                .entity(cliente)
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

        return Response
                .status(Response.Status.OK)
                .entity(cliente.get())
                .build();
    }

    @GET
    @Path("/buscar-por-nome/{nome}")
    public Response buscarClientePorNome(@PathParam("nome") String nome) {
        var panacheQuery = clienteRepository.findByNameLike(nome);


        return Response
                .status(Response.Status.OK)
                .entity(panacheQuery.list())
                .build();
    }
}
