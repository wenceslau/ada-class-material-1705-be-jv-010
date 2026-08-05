package com.ada.pedido.resources.exceptions;

import com.ada.pedido.services.pedido.PedidoException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.core.Response;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GenericExceptionMapperTest {

    private GenericExceptionMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new GenericExceptionMapper();
    }

    @Test
    @DisplayName("Deve mapear IllegalArgumentException para HTTP 400 Bad Request")
    void deveMapearIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Valor inválido");

        Response response = mapper.toResponse(exception);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        ErrorResponse error = (ErrorResponse) response.getEntity();
        assertNotNull(error);
        assertEquals("Parametro invalido: Valor inválido", error.mensagem());
    }

    @Test
    @DisplayName("Deve mapear ConstraintViolationException para HTTP 400 Bad Request com mensagem padrão")
    void deveMapearConstraintViolationException() {
        ConstraintViolationException exception = new ConstraintViolationException("UK_VIOLATED", new SQLException(), "uk_constraint");

        Response response = mapper.toResponse(exception);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        ErrorResponse error = (ErrorResponse) response.getEntity();
        assertNotNull(error);
        assertEquals("Registro já existe", error.mensagem());
    }

    @Test
    @DisplayName("Deve mapear EntityNotFoundException para HTTP 404 Not Found")
    void deveMapearEntityNotFoundException() {
        EntityNotFoundException exception = new EntityNotFoundException("Cliente não encontrado");

        Response response = mapper.toResponse(exception);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        ErrorResponse error = (ErrorResponse) response.getEntity();
        assertNotNull(error);
        assertEquals("Cliente não encontrado", error.mensagem());
    }

    @Test
    @DisplayName("Deve mapear PedidoException para HTTP 400 Bad Request")
    void deveMapearPedidoException() {
        PedidoException exception = new PedidoException("Estoque insuficiente");

        Response response = mapper.toResponse(exception);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        ErrorResponse error = (ErrorResponse) response.getEntity();
        assertNotNull(error);
        assertEquals("Estoque insuficiente", error.mensagem());
    }

    @Test
    @DisplayName("Deve mapear exceção genérica Throwable para HTTP 500 Internal Server Error")
    void deveMapearThrowableGenerico() {
        RuntimeException exception = new RuntimeException("Erro inesperado no servidor");

        Response response = mapper.toResponse(exception);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        ErrorResponse error = (ErrorResponse) response.getEntity();
        assertNotNull(error);
        assertEquals("Erro inesperado no servidor", error.mensagem());
    }
}
