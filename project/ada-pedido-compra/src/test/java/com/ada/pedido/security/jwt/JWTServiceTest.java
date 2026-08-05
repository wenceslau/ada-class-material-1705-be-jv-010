package com.ada.pedido.security.jwt;

import io.smallrye.jwt.auth.principal.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JWTServiceTest {

    private JWTService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JWTService();
    }

    @Test
    @DisplayName("Deve criar e validar token JWT com sucesso")
    void deveCriarEValidarTokenComSucesso() {
        String username = "usuario@teste.com";
        Set<String> roles = Set.of("CLIENTE");

        String token = jwtService.criarToken(username, roles);

        assertNotNull(token);
        assertFalse(token.isBlank());
        assertDoesNotThrow(() -> jwtService.validarToken(token));
    }

    @Test
    @DisplayName("Deve lançar ParseException ao validar token inválido")
    void deveLancarExcecaoParaTokenInvalido() {
        String tokenInvalido = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.invalid.signature";

        assertThrows(ParseException.class, () -> jwtService.validarToken(tokenInvalido));
    }
}
