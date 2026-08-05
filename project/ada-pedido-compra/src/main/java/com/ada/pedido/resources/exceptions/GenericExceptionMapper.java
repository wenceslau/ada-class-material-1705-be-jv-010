package com.ada.pedido.resources.exceptions;

import com.ada.pedido.services.pedido.PedidoException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.hibernate.exception.ConstraintViolationException;

import java.time.LocalDateTime;


@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {

        return switch (exception) {
            case IllegalArgumentException iae -> Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(
                            iae.getClass().getName(),
                            "Parametro invalido: " + iae.getMessage(),
                            LocalDateTime.now()
                    ))
                    .build();
            case ConstraintViolationException cve -> Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(
                            cve.getClass().getName(),
                            "Registro já existe",
                            LocalDateTime.now()
                    ))
                    .build();
            case EntityNotFoundException enfe -> Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(
                            enfe.getClass().getName(),
                            enfe.getMessage(),
                            LocalDateTime.now()
                    ))
                    .build();
            case PedidoException pe -> Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(
                            pe.getClass().getName(),
                            pe.getMessage(),
                            LocalDateTime.now()
                    ))
                    .build();
            case Throwable t -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse(
                            t.getClass().getName(),
                            t.getMessage(),
                            LocalDateTime.now()
                    ))
                    .build();
        };
    }
}
