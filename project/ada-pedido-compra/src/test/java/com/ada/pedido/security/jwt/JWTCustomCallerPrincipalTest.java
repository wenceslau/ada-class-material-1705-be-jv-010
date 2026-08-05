package com.ada.pedido.security.jwt;

import io.smallrye.jwt.auth.principal.JWTCallerPrincipal;
import io.smallrye.jwt.auth.principal.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class JWTCustomCallerPrincipalTest {

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private JWTCustomCallerPrincipal customCallerPrincipal;

    private JWTService realJwtService;

    @BeforeEach
    void setUp() {
        realJwtService = new JWTService();
    }

    @Test
    @DisplayName("Deve realizar parse do token JWT e retornar JWTCallerPrincipal")
    void deveFazerParseDoTokenComSucesso() throws ParseException {
        doNothing().when(jwtService).validarToken(anyString());

        String validToken = realJwtService.criarToken("usuario@teste.com", Set.of("CLIENTE"));

        JWTCallerPrincipal principal = customCallerPrincipal.parse(validToken, null);

        assertNotNull(principal);
        assertEquals("usuario@teste.com", principal.getName());
    }

    @Test
    @DisplayName("Deve lançar ParseException se o token for malformado ou inválido")
    void deveLancarParseExceptionTokenMalformado() {
        String tokenInvalido = "invalid.token";

        assertThrows(ParseException.class, () -> customCallerPrincipal.parse(tokenInvalido, null));
    }
}
