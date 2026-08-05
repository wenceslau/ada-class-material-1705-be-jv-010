package com.ada.pedido.repositories;

import com.ada.pedido.repositories.entities.ProdutoEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ProdutoRepository implements PanacheRepositoryBase<ProdutoEntity, Long> {

    public List<ProdutoEntity> findByDescricaoLikeIgnoreCase(String filtro) {
        return find("lower(descricao) like lower(concat('%', ?1, '%'))", filtro)
                .list();
    }


}
