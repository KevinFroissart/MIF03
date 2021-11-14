package fr.univlyon1.m1if.m1if03.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class JWTHelper {
    private static final String SECRET = ";}72>zkUq#{5kt]K";
    private static final String ISSUER = "iss";
    private static final long LIFETIME = 1800000; // 30 min
    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET);

    public static String verifyToken(String token) throws NullPointerException, JWTVerificationException {
        JWTVerifier authenticationVerifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build();

        authenticationVerifier.verify(token);
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("sub").asString();
    }

    public static boolean verifyAdmin(String token) {
        try {
            JWT.require(algorithm).withClaim("admin", true).build().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public static String generateToken(String login, boolean admin) throws JWTCreationException {
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(login)
                .withClaim("admin", admin)
                .withExpiresAt(new Date(new Date().getTime() + LIFETIME))
                .sign(algorithm);
    }

}
