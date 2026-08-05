package com.ada.pedido.resources.dto;


import com.ada.pedido.repositories.entities.ClienteEntity;
import com.ada.pedido.repositories.entities.TipoUsuario;

public record ClienteResponseDTO(
        Long id,
        String nome,
        String email,
        TipoUsuario tipoUsuario) {

    public static ClienteResponseDTO criarDeEntidade(ClienteEntity cliente) {
        if (cliente == null) {
            return null;
        }
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getTipoUsuario()
        );
    }

}
