package com.ada.pedido.security;

import com.ada.pedido.repositories.ClienteRepository;
import com.ada.pedido.security.jwt.JWTService;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.Set;

@Path("/login")
@Tag(name = "Autenticação", description = "Endpoints de autenticação e emissão de tokens JWT")
public class LoginResource {

    private final JWTService jwtService;
    private final ClienteRepository clienteRepository;

    public LoginResource(JWTService jwtService, ClienteRepository clienteRepository) {
        this.jwtService = jwtService;
        this.clienteRepository = clienteRepository;
    }

    @POST
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Autenticar usuário (Login)", description = "Autentica o cliente/administrador por e-mail e senha e retorna o Token JWT para acesso aos demais endpoints.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Autenticado com sucesso. Retorna o Token JWT"),
            @APIResponse(responseCode = "401", description = "Credenciais inválidas (e-mail ou senha incorretos)")
    })
    public Response login(@Valid LoginRequest loginDto) {

        var optional = clienteRepository.buscarPorEmail(loginDto.email());
        if (optional.isEmpty()) {
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .build();
        }
        var cliente = optional.get();
        if (!BcryptUtil.matches(loginDto.senha(), cliente.getSenha())) {
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .build();
        }

        String token = jwtService.criarToken(
                cliente.getEmail(), Set.of(cliente.getTipoUsuario().name())
        );

        return Response.ok(new LoginResponse(token))
                .build();
    }

}
