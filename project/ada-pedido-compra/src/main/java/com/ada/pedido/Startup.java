package com.ada.pedido;

import com.ada.pedido.repositories.ClienteRepository;
import com.ada.pedido.repositories.ProdutoRepository;
import com.ada.pedido.repositories.entities.ClienteEntity;
import com.ada.pedido.repositories.entities.ProdutoEntity;
import com.ada.pedido.repositories.entities.TipoUsuario;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;

@Singleton
public class Startup {

    @Transactional
    public void criarAdmin(@Observes StartupEvent evt, ClienteRepository repository) {
        if (repository.buscarPorEmail("admin@ada.com").isEmpty()) {
            ClienteEntity admin = new ClienteEntity();
            admin.setNome("Admin");
            admin.setEmail("admin@ada.com");
            admin.setSenha(BcryptUtil.bcryptHash("12345678"));
            admin.setTipoUsuario(TipoUsuario.ADMIN);
            repository.persist(admin);
        }
    }

    @Transactional
    public void criarProduto(@Observes StartupEvent evt, ProdutoRepository repository) {
        if (repository.findByDescricaoLikeIgnoreCase("Produto 1").isEmpty()) {
            ProdutoEntity produto = new ProdutoEntity();
            produto.setDescricao("Produto 1");
            produto.setPreco(BigDecimal.valueOf(35.99));
            produto.setEstoque(8);
            repository.persist(produto);
        }
        if (repository.findByDescricaoLikeIgnoreCase("Produto 2").isEmpty()) {
            ProdutoEntity produto = new ProdutoEntity();
            produto.setDescricao("Produto 2");
            produto.setPreco(BigDecimal.valueOf(45.99));
            produto.setEstoque(12);
            repository.persist(produto);
        }

    }
}
