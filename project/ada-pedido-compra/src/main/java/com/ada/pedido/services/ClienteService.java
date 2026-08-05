package com.ada.pedido.services;

import com.ada.pedido.repositories.ClienteRepository;
import com.ada.pedido.repositories.entities.ClienteEntity;
import com.ada.pedido.repositories.entities.TipoUsuario;
import com.ada.pedido.resources.dto.ClienteRequestDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public ClienteEntity criarCliente(ClienteRequestDTO clienteDTO) {

        var cliente = clienteDTO.criarEntidade();

        // RN: O cadastro publico sempre cria um CLIENTE, nunca um ADMIN
        cliente.setTipoUsuario(TipoUsuario.CLIENTE);

        validarCliente(cliente);

        clienteRepository.persist(cliente);

        return cliente;
    }

    @Transactional
    public ClienteEntity atualizarCliente(Long id, ClienteRequestDTO clienteDTO) {

        var cliente = buscarClientePorId(id);
        clienteDTO.copiarParaEntidade(cliente);

        validarCliente(cliente);

        clienteRepository.persist(cliente);

        return cliente;
    }

    @Transactional
    public ClienteEntity atualizarClienteParcial(Long id, ClienteRequestDTO clienteDTO) {

        var cliente = buscarClientePorId(id);
        clienteDTO.copiarParaEntidadeNaoNulo(cliente);

        validarCliente(cliente);

        clienteRepository.persist(cliente);

        return cliente;
    }

    @Transactional
    public void deletarCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    public ClienteEntity buscarClientePorId(Long id) {
        return clienteRepository.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado!"));
    }

    public List<ClienteEntity> buscarTodos(){
        return clienteRepository.listAll();
    }

    private static void validarCliente(ClienteEntity cliente) {
        if (cliente.getNome() == null || cliente.getNome().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio!");
        }
        if (cliente.getEmail() == null || cliente.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser vazio!");
        }
        if (cliente.getSenha() == null || cliente.getSenha().isEmpty() || cliente.getSenha().length() < 8) {
            throw new IllegalArgumentException("Senha não pode ser vazia e deve ter no mínimo 8 caracteres!");
        }
    }

}
