package com.ada.pedido.repositories;

import com.ada.pedido.repositories.entities.ClienteEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class ClienteRepository implements PanacheRepositoryBase<ClienteEntity, Long> {

    public Optional<ClienteEntity> buscarPorEmail(String email) {
        return find("email", email)
                .firstResultOptional();
    }

}
