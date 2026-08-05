package com.ada.pedido.resources;

import com.ada.pedido.repositories.entities.ProdutoEntity;
import com.ada.pedido.resources.dto.ProdutoRequestDTO;
import com.ada.pedido.services.ProdutoService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoResourceTest {

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private ProdutoResource produtoResource;

    private ProdutoEntity produto;
    private ProdutoRequestDTO dto;

    @BeforeEach
    void setUp() {
        dto = new ProdutoRequestDTO("Smartphone", new BigDecimal("2000.00"), 10);

        produto = new ProdutoEntity();
        produto.setId(1L);
        produto.setDescricao("Smartphone");
        produto.setPreco(new BigDecimal("2000.00"));
        produto.setEstoque(10);
    }

    @Test
    @DisplayName("POST /produtos - Deve criar produto e retornar HTTP 201 Created")
    void deveCriarProdutoComStatus201() {
        when(produtoService.criarProduto(any(ProdutoRequestDTO.class))).thenReturn(produto);

        Response response = produtoResource.criar(dto);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
        verify(produtoService).criarProduto(dto);
    }

    @Test
    @DisplayName("PUT /produtos/{id} - Deve atualizar produto e retornar HTTP 200 OK")
    void deveAtualizarProduto() {
        when(produtoService.atualizarProduto(eq(1L), any(ProdutoRequestDTO.class))).thenReturn(produto);

        Response response = produtoResource.atualizar(1L, dto);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(produtoService).atualizarProduto(1L, dto);
    }

    @Test
    @DisplayName("PATCH /produtos/{id} - Deve atualizar produto parcialmente e retornar HTTP 200 OK")
    void deveAtualizarProdutoParcialmente() {
        when(produtoService.atualizarProdutoParcial(eq(1L), any(ProdutoRequestDTO.class))).thenReturn(produto);

        Response response = produtoResource.atualizacaoParcial(1L, dto);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(produtoService).atualizarProdutoParcial(1L, dto);
    }

    @Test
    @DisplayName("GET /produtos - Deve listar produtos e retornar HTTP 200 OK")
    void deveListarProdutos() {
        when(produtoService.buscarTodos()).thenReturn(List.of(produto));

        Response response = produtoResource.listar();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(produtoService).buscarTodos();
    }

    @Test
    @DisplayName("GET /produtos/{produtoId} - Deve buscar produto por ID e retornar HTTP 200 OK")
    void deveBuscarProdutoPorId() {
        when(produtoService.buscarProdutoPorId(1L)).thenReturn(produto);

        Response response = produtoResource.buscarPorId(1L);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(produtoService).buscarProdutoPorId(1L);
    }

    @Test
    @DisplayName("GET /produtos/procurar/{descricao} - Deve buscar produto por descrição e retornar HTTP 200 OK")
    void deveBuscarProdutoPorDescricao() {
        when(produtoService.buscarProdutoPorDescricao("Smart")).thenReturn(List.of(produto));

        Response response = produtoResource.buscarPorDescricao("Smart");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(produtoService).buscarProdutoPorDescricao("Smart");
    }
}
