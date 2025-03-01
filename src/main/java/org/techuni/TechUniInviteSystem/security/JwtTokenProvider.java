package org.techuni.TechUniInviteSystem.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    @Value("${jwt.issuer}")
    private String issuer;

    private final Algorithm algorithm;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    @Autowired
    public JwtTokenProvider(@Value("${jwt.secret}") String jwtSecret) {
        this.algorithm = Algorithm.HMAC512(jwtSecret);
    }

    // JWTトークンを生成
    public String generateToken(Authentication authentication) {
        final var userPrincipal = (UserDetails) authentication.getPrincipal();

        final var now = new Date();
        final var expiryDate = Optional.ofNullable(jwtExpirationInMs > 0 ? new Date(now.getTime() + jwtExpirationInMs) : null);

        final var tokenBuilder = JWT.create() //
                .withIssuer(issuer) //
                .withSubject(userPrincipal.getUsername()) //
                .withIssuedAt(now);
        expiryDate.ifPresent(tokenBuilder::withExpiresAt);

        return tokenBuilder.sign(algorithm);
    }

    // JWTトークンを検証
    public Optional<DecodedJWT> decode(String token) {
        final var verifier = JWT.require(algorithm) //
                .withIssuer(issuer) //
                .build();
        try {
            return Optional.ofNullable(verifier.verify(token));
        } catch (JWTVerificationException e) {
            return Optional.empty();
        }
    }
}
