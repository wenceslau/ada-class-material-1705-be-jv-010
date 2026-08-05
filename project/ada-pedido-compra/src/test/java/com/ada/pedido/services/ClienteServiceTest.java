package com.ada.pedido.services;

import com.ada.pedido.repositories.ClienteRepository;
import com.ada.pedido.repositories.entities.ClienteEntity;
import com.ada.pedido.repositories.entities.TipoUsuario;
import com.ada.pedido.resources.dto.ClienteRequestDTO;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private ClienteRequestDTO validDTO;

    @BeforeEach
    void setUp() {
        validDTO = new ClienteRequestDTO("João Silva", "joao@email.com", "senha12345");
    }

    @Nested
    @DisplayName("Testes para criarCliente")
    class CriarClienteTests {

        @Test
        @DisplayName("Deve criar cliente com sucesso e atribuir tipo CLIENTE")
        void deveCriarClienteComSucesso() {
            ClienteEntity resultado = clienteService.criarCliente(validDTO);

            assertNotNull(resultado);
            assertEquals("João Silva", resultado.getNome());
            assertEquals("joao@email.com", resultado.getEmail());
            assertEquals(TipoUsuario.CLIENTE, resultado.getTipoUsuario());
            verify(clienteRepository, times(1)).persist(any(ClienteEntity.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando o nome estiver vazio")
        void deveLancarExcecaoQuandoNomeVazio() {
            ClienteRequestDTO dtoInvalido = new ClienteRequestDTO("", "joao@email.com", "senha12345");

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> clienteService.criarCliente(dtoInvalido)
            );

            assertEquals("Nome não pode ser vazio!", exception.getMessage());
            verify(clienteRepository, never()).persist(any(ClienteEntity.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando o email estiver vazio")
        void deveLancarExcecaoQuandoEmailVazio() {
            ClienteRequestDTO dtoInvalido = new ClienteRequestDTO("João", "", "senha12345");

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> clienteService.criarCliente(dtoInvalido)
            );

            assertEquals("Email não pode ser vazio!", exception.getMessage());
            verify(clienteRepository, never()).persist(any(ClienteEntity.class));
        }
    }

    @Nested
    @DisplayName("Testes para atualizarCliente")
    class AtualizarClienteTests {

        @Test
        @DisplayName("Deve atualizar cliente existente com sucesso")
        void deveAtualizarClienteComSucesso() {
            Long id = 1L;
            ClienteEntity existente = new ClienteEntity();
            existente.setId(id);
            existente.setNome("Antigo");
            existente.setEmail("antigo@email.com");
            existente.setSenha("senhaAntiga123");

            when(clienteRepository.findByIdOptional(id)).thenReturn(Optional.of(existente));

            ClienteRequestDTO novoDTO = new ClienteRequestDTO("Novo Nome", "novo@email.com", "novasenha123");

            ClienteEntity atualizado = clienteService.atualizarCliente(id, novoDTO);

            assertEquals("Novo Nome", atualizado.getNome());
            assertEquals("novo@email.com", atualizado.getEmail());
            verify(clienteRepository).persist(existente);
        }

        @Test
        @DisplayName("Deve lançar EntityNotFoundException quando cliente não for encontrado")
        void deveLancarExcecaoQuandoClienteNaoEncontrado() {
            Long id = 99L;
            when(clienteRepository.findByIdOptional(id)).thenReturn(Optional.empty());

            assertThrows(
                    EntityNotFoundException.class,
                    () -> clienteService.atualizarCliente(id, validDTO)
            );
        }
    }

    @Nested
    @DisplayName("Testes para atualizarClienteParcial")
    class AtualizarClienteParcialTests {

        @Test
        @DisplayName("Deve atualizar parcialmente campos não nulos")
        void deveAtualizarParcialmente() {
            Long id = 1L;
            ClienteEntity existente = new ClienteEntity();
            existente.setId(id);
            existente.setNome("Nome Original");
            existente.setEmail("original@email.com");
            existente.setSenha("$2a$10$abcdefghijklmnopqrstuvwxyz1234567890123456789012"); // hash com >= 8 chars

            when(clienteRepository.findByIdOptional(id)).thenReturn(Optional.of(existente));

            ClienteRequestDTO dtoParcial = new ClienteRequestDTO("Novo Nome Apenas", null, null);

            ClienteEntity resultado = clienteService.atualizarClienteParcial(id, dtoParcial);

            assertEquals("Novo Nome Apenas", resultado.getNome());
            assertEquals("original@email.com", resultado.getEmail());
            verify(clienteRepository).persist(existente);
        }
    }

    @Nested
    @DisplayName("Testes para buscarClientePorId e buscarTodos")
    class BuscarClienteTests {

        @Test
        @DisplayName("Deve buscar cliente por ID com sucesso")
        void deveBuscarClientePorId() {
            Long id = 1L;
            ClienteEntity cliente = new ClienteEntity();
            cliente.setId(id);

            when(clienteRepository.findByIdOptional(id)).thenReturn(Optional.of(cliente));

            ClienteEntity resultado = clienteService.buscarClientePorId(id);

            assertNotNull(resultado);
            assertEquals(id, resultado.getId());
        }

        @Test
        @DisplayName("Deve lançar EntityNotFoundException quando ID não existir")
        void deveLancarExcecaoAoBuscarIdInexistente() {
            when(clienteRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () -> clienteService.buscarClientePorId(1L));
        }

        @Test
        @DisplayName("Deve buscar todos os clientes")
        void deveBuscarTodos() {
            List<ClienteEntity> lista = List.of(new ClienteEntity(), new ClienteEntity());
            when(clienteRepository.listAll()).thenReturn(lista);

            List<ClienteEntity> resultado = clienteService.buscarTodos();

            assertEquals(2, resultado.size());
            verify(clienteRepository).listAll();
        }

        @Test
        @DisplayName("Deve deletar cliente por ID")
        void deveDeletarCliente() {
            Long id = 1L;
            clienteService.deletarCliente(id);
            verify(clienteRepository).deleteById(id);
        }
    }
}
