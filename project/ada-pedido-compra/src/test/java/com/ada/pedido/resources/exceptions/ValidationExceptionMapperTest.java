package com.ada.pedido.resources.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ValidationExceptionMapperTest {

    private ValidationExceptionMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ValidationExceptionMapper();
    }

    @Test
    @DisplayName("Deve mapear ConstraintViolationException para HTTP 400 Bad Request concatenando mensagens")
    void deveMapearValidationException() {
        ConstraintViolation<?> v1 = mock(ConstraintViolation.class);
        when(v1.getMessage()).thenReturn("Nome é obrigatório");

        ConstraintViolation<?> v2 = mock(ConstraintViolation.class);
        when(v2.getMessage()).thenReturn("Email inválido");

        ConstraintViolationException exception = new ConstraintViolationException(Set.of(v1, v2));

        Response response = mapper.toResponse(exception);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        ErrorResponse error = (ErrorResponse) response.getEntity();
        assertNotNull(error);
    }
}
