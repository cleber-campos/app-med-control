package app.shared.security;

import app.models.Usuario;
import app.shared.utils.TimeZoneConfig;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secretKey;

    public String gerarToken(Usuario usuario){
        try {
            var algoritimo = Algorithm.HMAC256(secretKey);
            return JWT.create()
                    .withIssuer("API.MED")
                    .withSubject(usuario.getLogin())
                    .withClaim("ID", usuario.getId())
                    .withClaim("LOGIN", usuario.getLogin())
                    .withExpiresAt(gerarDataExpiracao())
                    .sign(algoritimo);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar o Token", exception);
        }
    }

    public String recuperarLogin(String token){
        try {
            var algoritimo = Algorithm.HMAC256(secretKey);
            return JWT.require(algoritimo)
                    .withIssuer("API.MED")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
           throw new RuntimeException("Token invalido: " + token );
        }
    }

    public Instant recuperarDataExpiracao(String token) {
        try {
            var algoritimo = Algorithm.HMAC256(secretKey);
            return JWT.require(algoritimo)
                    .withIssuer("API.MED")
                    .build()
                    .verify(token)
                    .getExpiresAtAsInstant();
        } catch (JWTVerificationException exception){
            throw new RuntimeException("Token expirado: " + token );
        }
    }

    public Boolean validaTokenExpirado(String token){
        var dataExpiracaoToken = recuperarDataExpiracao(token);
        var dataAtual = LocalDateTime.now().toInstant(ZoneOffset.of("-03:00"));
        return dataExpiracaoToken.isBefore(dataAtual);
    }

    private Instant gerarDataExpiracao() {
        return LocalDateTime.now().plusMinutes(10).toInstant(TimeZoneConfig.DEFAULT_ZONE_OFFSET);
    }
}
