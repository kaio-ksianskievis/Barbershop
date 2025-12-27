package kaio.ksianskievis.barbershop.Services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import kaio.ksianskievis.barbershop.Model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private  String key;

    public String generateToken(User usuario){
        try {
            Instant tempo = LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
            Algorithm algorithm = Algorithm.HMAC256(key);
            String token = JWT.create()
                    .withIssuer("barbershop-api")
                    .withSubject(usuario.getEmail())
                    .withExpiresAt(tempo)
                    .sign(algorithm);
            return  token;
        }catch(JWTCreationException e){
            throw  new RuntimeException("Erro ao gerar toke",e);
        }
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(key);
            return JWT.require(algorithm)
                    .withIssuer("barbershop-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            return "";
        }
    }
}
