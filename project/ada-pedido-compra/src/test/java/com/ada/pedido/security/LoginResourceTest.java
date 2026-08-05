package com.ada.pedido.security;

import com.ada.pedido.repositories.ClienteRepository;
import com.ada.pedido.repositories.entities.ClienteEntity;
import com.ada.pedido.repositories.entities.TipoUsuario;
import com.ada.pedido.security.jwt.JWTService;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginResourceTest {

    @Mock
    private JWTService jwtService;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private LoginResource loginResource;

    private ClienteEntity cliente;

    @BeforeEach
    void setUp() {
        cliente = new ClienteEntity();
        cliente.setEmail("teste@email.com");
        cliente.setSenha(BcryptUtil.bcryptHash("senha12345"));
        cliente.setTipoUsuario(TipoUsuario.CLIENTE);
    }

    @Test
    @DisplayName("Deve realizar login com sucesso e retornar HTTP 200 com Token")
    void deveRealizarLoginComSucesso() {
        LoginRequest request = new LoginRequest("teste@email.com", "senha12345");

        when(clienteRepository.buscarPorEmail("teste@email.com")).thenReturn(Optional.of(cliente));
        when(jwtService.criarToken(eq("teste@email.com"), anySet())).thenReturn("mocked.jwt.token");

        Response response = loginResource.login(request);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof LoginResponse);
        assertEquals("mocked.jwt.token", ((LoginResponse) response.getEntity()).token());
    }

    @Test
    @DisplayName("Deve retornar HTTP 401 Unauthorized se e-mail não for encontrado")
    void deveRetornar401QuandoEmailNaoEncontrado() {
        LoginRequest request = new LoginRequest("naoexiste@email.com", "senha12345");

        when(clienteRepository.buscarPorEmail("naoexiste@email.com")).thenReturn(Optional.empty());

        Response response = loginResource.login(request);

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }

    @Test
    @DisplayName("Deve retornar HTTP 401 Unauthorized se a senha estiver incorreta")
    void deveRetornar401QuandoSenhaIncorreta() {
        LoginRequest request = new LoginRequest("teste@email.com", "senhaErrada");

        when(clienteRepository.buscarPorEmail("teste@email.com")).thenReturn(Optional.of(cliente));

        Response response = loginResource.login(request);

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }
}
