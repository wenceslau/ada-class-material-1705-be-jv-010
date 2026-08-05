package com.ada.pedido.services.pedido;

import com.ada.pedido.repositories.entities.PedidoEntity;
import com.ada.pedido.repositories.entities.StatusPedido;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.format.DateTimeFormatter;

@Priority(10)
@ApplicationScoped
public class PedidoEnvioEmail implements ProcessarPedido {

    @Override
    public void processar(PedidoEntity pedido) {
        System.out.println("Etapa enviar email");

        // Try catch to avoid any rollback on the transaction
        try {
            var mensagem = criarMensagem(pedido);
            var assunto = "Pedido " + pedido.getId() + " - " + pedido.getStatus();

            System.out.printf("Enviando email para o cliente: %s com assunto: %s", pedido.getCliente().getEmail(), assunto);
            System.out.println(mensagem);

            // simulate email sent....
        } catch (Exception e) {
            System.err.println("Erro ao enviar email: " + e.getMessage());
        }

    }

    private String criarMensagem(final PedidoEntity pedido) {
        var cliente = pedido.getCliente();

        var dataPedido = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(pedido.getDataPedido());

        final StringBuilder mensagem = new StringBuilder();
        mensagem.append(cliente.getNome())
                .append(", ")
                .append("\n");

        mensagem.append("Data: ")
                .append(dataPedido)
                .append("\n");

        mensagem.append("Produtos: ")
                .append("\n");

        pedido.getItens().forEach(item -> mensagem
                .append(item.getQuantidade()).append("x ")
                .append(item.getProduto().getDescricao())
                .append("\n")
        );

        mensagem.append("Status: ")
                .append(pedido.getStatus())
                .append("\n");

        if (StatusPedido.NAO_PROCESSADO.equals(pedido.getStatus())) {
            mensagem.append(pedido.getMensagemStatus())
                    .append("\n");
        }

        return mensagem.toString();
    }

}
