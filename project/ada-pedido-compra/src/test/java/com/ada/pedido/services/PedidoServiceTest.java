package com.ada.pedido.services;

import com.ada.pedido.repositories.ClienteRepository;
import com.ada.pedido.repositories.PedidoRepository;
import com.ada.pedido.repositories.ProdutoRepository;
import com.ada.pedido.repositories.entities.ClienteEntity;
import com.ada.pedido.repositories.entities.PedidoEntity;
import com.ada.pedido.repositories.entities.ProdutoEntity;
import com.ada.pedido.repositories.entities.StatusPedido;
import com.ada.pedido.resources.dto.ItemPedidoRequestDTO;
import com.ada.pedido.resources.dto.PedidoRequestDTO;
import com.ada.pedido.services.pedido.PedidoException;
import com.ada.pedido.services.pedido.ProcessarPedido;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.inject.Instance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private Instance<ProcessarPedido> listaProcessarPedido;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private SecurityIdentity securityIdentity;

    @Mock
    private Principal principal;

    @Mock
    private ProcessarPedido stepProcessar;

    @InjectMocks
    private PedidoService pedidoService;

    private ClienteEntity cliente;
    private ProdutoEntity produto;

    @BeforeEach
    void setUp() {
        cliente = new ClienteEntity();
        cliente.setId(1L);
        cliente.setEmail("usuario@teste.com");

        produto = new ProdutoEntity();
        produto.setId(10L);
        produto.setPreco(new BigDecimal("100.00"));
        produto.setEstoque(5);
    }

    @Nested
    @DisplayName("Testes para criação de pedido")
    class CriarPedidoTests {

        @Test
        @DisplayName("Deve criar pedido com sucesso executando as etapas de processamento")
        void deveCriarPedidoComSucesso() {
            when(securityIdentity.getPrincipal()).thenReturn(principal);
            when(principal.getName()).thenReturn("usuario@teste.com");
            when(clienteRepository.buscarPorEmail("usuario@teste.com")).thenReturn(Optional.of(cliente));
            when(produtoRepository.findByIdOptional(10L)).thenReturn(Optional.of(produto));
            when(listaProcessarPedido.iterator()).thenReturn(List.of(stepProcessar).iterator());

            PedidoRequestDTO dto = new PedidoRequestDTO(List.of(new ItemPedidoRequestDTO(10L, 2)));

            PedidoEntity pedidoCriado = pedidoService.create(dto);

            assertNotNull(pedidoCriado);
            assertEquals(StatusPedido.NOVO, pedidoCriado.getStatus());
            assertEquals(cliente, pedidoCriado.getCliente());
            assertEquals(1, pedidoCriado.getItens().size());
            assertEquals(new BigDecimal("100.00"), pedidoCriado.getItens().get(0).getPreco());

            verify(stepProcessar, times(1)).processar(pedidoCriado);
        }

        @Test
        @DisplayName("Deve lançar PedidoException se cliente autenticado não for encontrado no banco")
        void deveLancarExcecaoQuandoClienteNaoEncontrado() {
            when(securityIdentity.getPrincipal()).thenReturn(principal);
            when(principal.getName()).thenReturn("desconhecido@teste.com");
            when(clienteRepository.buscarPorEmail("desconhecido@teste.com")).thenReturn(Optional.empty());

            PedidoRequestDTO dto = new PedidoRequestDTO(List.of(new ItemPedidoRequestDTO(10L, 1)));

            PedidoException exception = assertThrows(
                    PedidoException.class,
                    () -> pedidoService.create(dto)
            );

            assertEquals("Usuario não encontrado!", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar PedidoException se produto do item não for encontrado")
        void deveLancarExcecaoQuandoProdutoNaoEncontrado() {
            when(securityIdentity.getPrincipal()).thenReturn(principal);
            when(principal.getName()).thenReturn("usuario@teste.com");
            when(clienteRepository.buscarPorEmail("usuario@teste.com")).thenReturn(Optional.of(cliente));
            when(produtoRepository.findByIdOptional(99L)).thenReturn(Optional.empty());

            PedidoRequestDTO dto = new PedidoRequestDTO(List.of(new ItemPedidoRequestDTO(99L, 1)));

            PedidoException exception = assertThrows(
                    PedidoException.class,
                    () -> pedidoService.create(dto)
            );

            assertTrue(exception.getMessage().contains("Produto não encontrado! Id: 99"));
        }
    }

    @Nested
    @DisplayName("Testes para listagem de pedidos")
    class ListarPedidosTests {

        @Test
        @DisplayName("Deve listar todos os pedidos")
        void deveListarTodosPedidos() {
            List<PedidoEntity> lista = List.of(new PedidoEntity(), new PedidoEntity());
            when(pedidoRepository.listAll()).thenReturn(lista);

            List<PedidoEntity> resultado = pedidoService.listarTodos();

            assertEquals(2, resultado.size());
            verify(pedidoRepository).listAll();
        }
    }
}
