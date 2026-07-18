package it.application.nationaldefencemanagementsystem.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import it.application.nationaldefencemanagementsystem.Entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTTools {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    //Converte la chiave Base64 in una SecretKey.

    private SecretKey getKey() {

        byte[] keyBytes =
                Decoders.BASE64.decode(secret);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Genera il token e inserisce l'id utente

    public String generateToken(User user) {

        Date now = new Date();

        Date expirationDate =
                new Date(
                        now.getTime() + expiration
                );

        return Jwts.builder()
                .subject(
                        String.valueOf(
                                user.getId()
                        )
                )
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(getKey())
                .compact();
    }

    // Verifica il token e restituisce l'id dell'utente.

    public Integer extractUserId(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return Integer.valueOf(
                claims.getSubject()
        );
    }
}