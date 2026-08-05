package com.ada.pedido.security.jwt;

import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import io.smallrye.jwt.auth.principal.JWTCallerPrincipal;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.logging.Logger;

@ApplicationScoped
public class JWTService {

    private static final Logger log = Logger.getLogger(JWTService.class.getName());
    private static final String ISSUER = "https://ada.com";
    private static final String SECRET = "MySuperSecretKeyForTheAdaCourseThatIsAtLeast256BitsLong";

    public String criarToken(String username, Set<String> roles) {
        JwtClaimsBuilder claimsBuilder = Jwt.claims()
                .issuer(ISSUER)
                .subject(username)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(1800)) //0.5 hora
                .groups(roles);

        return claimsBuilder.jws().signWithSecret(SECRET);
    }

    public void validarToken(String token) throws ParseException {
        SignedJWT signedJWT;
        try {
            signedJWT = SignedJWT.parse(token);

            if (!signedJWT.verify(new MACVerifier(SECRET))) {
                throw new RuntimeException("Invalid JWT signature");
            }

            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            if (expirationTime != null && expirationTime.before(new Date())) {
                throw new RuntimeException("Token expired");
            }

        } catch (Exception e) {
            log.warning("Error validate token: " + e.getMessage());
            throw new ParseException("Error validate token", e);
        }
    }


}
