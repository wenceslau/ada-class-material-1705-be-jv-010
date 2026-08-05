package com.ada.pedido.resources;

import com.ada.pedido.resources.dto.ProdutoRequestDTO;
import com.ada.pedido.resources.dto.ProdutoResponseDTO;
import com.ada.pedido.services.ProdutoService;
import io.quarkus.security.Authenticated;
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
@Path("/produtos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Produtos", description = "Endpoints para gerenciamento do catálogo de produtos e estoque")
public class ProdutoResource {

    private final ProdutoService produtoService;

    public ProdutoResource(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @POST
    @RolesAllowed("ADMIN")
    @Operation(summary = "Criar produto", description = "Cadastra um novo produto no catálogo. Requer perfil ADMIN.")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @APIResponse(responseCode = "400", description = "Dados do produto inválidos"),
            @APIResponse(responseCode = "401", description = "Não autenticado"),
            @APIResponse(responseCode = "403", description = "Acesso negado - Requer perfil ADMIN")
    })
    public Response criar(@Valid ProdutoRequestDTO produtoRequestDTO) {

        var produtoCriado = produtoService.criarProduto(produtoRequestDTO);

        return Response
                .status(Response.Status.CREATED)
                .entity(ProdutoResponseDTO.criarDeEntidade(produtoCriado))
                .build();
    }

    @PUT
    @RolesAllowed("ADMIN")
    @Path("/{id}")
    @Operation(summary = "Atualizar produto por completo", description = "Atualiza todas as informações de um produto existente. Requer perfil ADMIN.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @APIResponse(responseCode = "400", description = "Dados inválidos"),
            @APIResponse(responseCode = "401", description = "Não autenticado"),
            @APIResponse(responseCode = "403", description = "Acesso negado - Requer perfil ADMIN"),
            @APIResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public Response atualizar(@Parameter(description = "ID do produto", required = true) @PathParam("id") Long id, @Valid ProdutoRequestDTO produtoRequestDTO) {

        var produto = produtoService.atualizarProduto(id, produtoRequestDTO);

        return Response
                .ok(ProdutoResponseDTO.criarDeEntidade(produto))
                .build();
    }

    @PATCH
    @RolesAllowed("ADMIN")
    @Path("/{id}")
    @Operation(summary = "Atualizar produto parcialmente", description = "Atualiza apenas os campos fornecidos do produto. Requer perfil ADMIN.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Produto atualizado parcialmente"),
            @APIResponse(responseCode = "401", description = "Não autenticado"),
            @APIResponse(responseCode = "403", description = "Acesso negado - Requer perfil ADMIN"),
            @APIResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public Response atualizacaoParcial(@Parameter(description = "ID do produto", required = true) @PathParam("id") Long id, ProdutoRequestDTO produtoRequestDTO) {

        var produto = produtoService.atualizarProdutoParcial(id, produtoRequestDTO);

        return Response
                .ok(ProdutoResponseDTO.criarDeEntidade(produto))
                .build();
    }

    @GET
    @RolesAllowed({"ADMIN", "CLIENTE"})
    @Operation(summary = "Listar todos os produtos", description = "Retorna a lista completa de produtos disponíveis. Requer autenticação (CLIENTE ou ADMIN).")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso"),
            @APIResponse(responseCode = "401", description = "Não autenticado")
    })
    public Response listar() {
        var listaDTO = produtoService.buscarTodos()
                .stream()
                .map(ProdutoResponseDTO::criarDeEntidade)
                .toList();

        return Response
                .ok(listaDTO)
                .build();
    }

    @GET
    @RolesAllowed({"ADMIN", "CLIENTE"})
    @Path("/{produtoId}")
    @Operation(summary = "Buscar produto por ID", description = "Retorna os detalhes de um produto específico através de seu ID. Requer autenticação (CLIENTE ou ADMIN).")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Produto encontrado"),
            @APIResponse(responseCode = "401", description = "Não autenticado"),
            @APIResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public Response buscarPorId(@Parameter(description = "ID do produto", required = true) @PathParam("produtoId") Long id) {
        return Response
                .ok(ProdutoResponseDTO.criarDeEntidade(produtoService.buscarProdutoPorId(id)))
                .build();
    }

    @GET
    @RolesAllowed({"ADMIN", "CLIENTE"})
    @Path("/procurar/{descricao}")
    @Operation(summary = "Buscar produtos por descrição", description = "Filtra produtos pelo nome/descrição (busca insensível a maiúsculas/minúsculas). Requer autenticação (CLIENTE ou ADMIN).")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Lista de produtos filtrados com sucesso"),
            @APIResponse(responseCode = "401", description = "Não autenticado")
    })
    public Response buscarPorDescricao(@Parameter(description = "Termo de busca na descrição", required = true) @PathParam("descricao") String filtro) {
        var listaDTO = produtoService.buscarProdutoPorDescricao(filtro)
                .stream()
                .map(ProdutoResponseDTO::criarDeEntidade)
                .toList();

        return Response
                .ok(listaDTO)
                .build();
    }

}
