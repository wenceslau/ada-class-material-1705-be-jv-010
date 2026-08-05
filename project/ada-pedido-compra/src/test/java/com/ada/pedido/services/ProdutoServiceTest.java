package com.ada.pedido.services;

import com.ada.pedido.repositories.ProdutoRepository;
import com.ada.pedido.repositories.entities.ProdutoEntity;
import com.ada.pedido.resources.dto.ProdutoRequestDTO;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    private ProdutoRequestDTO validDTO;

    @BeforeEach
    void setUp() {
        validDTO = new ProdutoRequestDTO("Notebook Gamer", new BigDecimal("4500.00"), 10);
    }

    @Nested
    @DisplayName("Testes para criarProduto")
    class CriarProdutoTests {

        @Test
        @DisplayName("Deve criar produto com sucesso")
        void deveCriarProdutoComSucesso() {
            ProdutoEntity resultado = produtoService.criarProduto(validDTO);

            assertNotNull(resultado);
            assertEquals("Notebook Gamer", resultado.getDescricao());
            assertEquals(new BigDecimal("4500.00"), resultado.getPreco());
            assertEquals(10, resultado.getEstoque());
            verify(produtoRepository, times(1)).persist(any(ProdutoEntity.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando a descrição for vazia")
        void deveLancarExcecaoQuandoDescricaoVazia() {
            ProdutoRequestDTO dtoInvalido = new ProdutoRequestDTO("", new BigDecimal("10.00"), 5);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> produtoService.criarProduto(dtoInvalido)
            );

            assertEquals("Descrição não pode ser vazia!", exception.getMessage());
            verify(produtoRepository, never()).persist(any(ProdutoEntity.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando o preço for menor ou igual a zero")
        void deveLancarExcecaoQuandoPrecoInvalido() {
            ProdutoRequestDTO dtoPrecoZero = new ProdutoRequestDTO("Mouse", BigDecimal.ZERO, 5);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> produtoService.criarProduto(dtoPrecoZero)
            );

            assertEquals("Preço deve ser maior que zero!", exception.getMessage());
            verify(produtoRepository, never()).persist(any(ProdutoEntity.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando o estoque for menor que zero")
        void deveLancarExcecaoQuandoEstoqueNegativo() {
            ProdutoRequestDTO dtoEstoqueNegativo = new ProdutoRequestDTO("Teclado", new BigDecimal("100.00"), -1);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> produtoService.criarProduto(dtoEstoqueNegativo)
            );

            assertEquals("Estoque deve ser maior ou igual a zero!", exception.getMessage());
            verify(produtoRepository, never()).persist(any(ProdutoEntity.class));
        }
    }

    @Nested
    @DisplayName("Testes para atualizarProduto")
    class AtualizarProdutoTests {

        @Test
        @DisplayName("Deve atualizar produto existente com sucesso")
        void deveAtualizarProdutoComSucesso() {
            Long id = 1L;
            ProdutoEntity existente = new ProdutoEntity();
            existente.setId(id);
            existente.setDescricao("Mouse Antigo");
            existente.setPreco(new BigDecimal("50.00"));
            existente.setEstoque(2);

            when(produtoRepository.findByIdOptional(id)).thenReturn(Optional.of(existente));

            ProdutoRequestDTO novoDTO = new ProdutoRequestDTO("Mouse Novo", new BigDecimal("80.00"), 15);

            ProdutoEntity atualizado = produtoService.atualizarProduto(id, novoDTO);

            assertEquals("Mouse Novo", atualizado.getDescricao());
            assertEquals(new BigDecimal("80.00"), atualizado.getPreco());
            assertEquals(15, atualizado.getEstoque());
            verify(produtoRepository).persist(existente);
        }

        @Test
        @DisplayName("Deve atualizar parcialmente produto com sucesso")
        void deveAtualizarParcialmente() {
            Long id = 1L;
            ProdutoEntity existente = new ProdutoEntity();
            existente.setId(id);
            existente.setDescricao("Monitor 24");
            existente.setPreco(new BigDecimal("900.00"));
            existente.setEstoque(5);

            when(produtoRepository.findByIdOptional(id)).thenReturn(Optional.of(existente));

            ProdutoRequestDTO dtoParcial = new ProdutoRequestDTO(null, new BigDecimal("850.00"), null);

            ProdutoEntity resultado = produtoService.atualizarProdutoParcial(id, dtoParcial);

            assertEquals("Monitor 24", resultado.getDescricao());
            assertEquals(new BigDecimal("850.00"), resultado.getPreco());
            assertEquals(5, resultado.getEstoque());
            verify(produtoRepository).persist(existente);
        }
    }

    @Nested
    @DisplayName("Testes para busca de produto")
    class BuscarProdutoTests {

        @Test
        @DisplayName("Deve buscar produto por ID com sucesso")
        void deveBuscarProdutoPorId() {
            Long id = 1L;
            ProdutoEntity produto = new ProdutoEntity();
            produto.setId(id);

            when(produtoRepository.findByIdOptional(id)).thenReturn(Optional.of(produto));

            ProdutoEntity resultado = produtoService.buscarProdutoPorId(id);

            assertNotNull(resultado);
            assertEquals(id, resultado.getId());
        }

        @Test
        @DisplayName("Deve lançar EntityNotFoundException quando produto não for encontrado")
        void deveLancarExcecaoQuandoProdutoNaoEncontrado() {
            Long id = 99L;
            when(produtoRepository.findByIdOptional(id)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () -> produtoService.buscarProdutoPorId(id));
        }

        @Test
        @DisplayName("Deve buscar produtos por descrição")
        void deveBuscarPorDescricao() {
            String filtro = "Gamer";
            List<ProdutoEntity> lista = List.of(new ProdutoEntity());
            when(produtoRepository.findByDescricaoLikeIgnoreCase(filtro)).thenReturn(lista);

            List<ProdutoEntity> resultado = produtoService.buscarProdutoPorDescricao(filtro);

            assertEquals(1, resultado.size());
            verify(produtoRepository).findByDescricaoLikeIgnoreCase(filtro);
        }

        @Test
        @DisplayName("Deve buscar todos os produtos")
        void deveBuscarTodos() {
            List<ProdutoEntity> lista = List.of(new ProdutoEntity(), new ProdutoEntity());
            when(produtoRepository.listAll()).thenReturn(lista);

            List<ProdutoEntity> resultado = produtoService.buscarTodos();

            assertEquals(2, resultado.size());
            verify(produtoRepository).listAll();
        }
    }
}
