package co.com.pragma.api.security.jwt.provider;

import co.com.pragma.api.security.exception.SecurityAppException;
import co.com.pragma.model.role.Role;
import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.model.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import utils.ErrorTypes;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;

@Component
public class JwtProvider {

//    @Value("${jwt.secret}")

//    @Value("${jwt.expiration}")
    private final Duration expiration;

    private SecretKey key;

    public JwtProvider(
            @Value("${jwt.secret}") String secretB64,
            @Value("${jwt.expiration}") long expiration
    ) {
        this.key = Keys.hmacShaKeyFor(secretB64.getBytes(StandardCharsets.UTF_8));
        this.expiration = Duration.ofSeconds(expiration);
    }

    public String generateToken(String userId, String email, Collection<Role> roles) {
        final Instant now = Instant.now();
        final Instant exp = now.plus(expiration);
        var roleNames = roles.stream().map(Role::getName).toList();

        return Jwts.builder()
                .subject(userId)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .claims(Map.of(
                        "role", roleNames,
                        "email", email,
                        "userId", userId
                ))
                .signWith(key)
                .compact();
    }

    public Claims getClaims(String token) {
        /*try {*/
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        /*} catch (ExpiredJwtException e) {
            throw new SecurityAppException(ErrorTypes.TOKEN_EXPIRED, e);
        } catch (UnsupportedJwtException e) {
            throw new SecurityAppException(ErrorTypes.TOKEN_UNSUPPORTED, e);
        } catch (MalformedJwtException e) {
            throw new SecurityAppException(ErrorTypes.TOKEN_INVALID, e);
        } catch (SignatureException e) {
            throw new SecurityAppException(ErrorTypes.TOKEN_SIGNATURE_INVALID, e);
        } catch (IllegalArgumentException e) {
            throw new SecurityAppException(ErrorTypes.TOKEN_INVALID, e);
        }*/

    }

    public String getSubject(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validate(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("expired");
        } catch (UnsupportedJwtException e) {
            System.out.println("token unsupported");
        } catch (MalformedJwtException e) {
            System.out.println("token malformed");
        } catch (SignatureException e) {
            System.out.println("bad signature");
        } catch (IllegalArgumentException e) {
            System.out.println("illegal args");
        }
        return false;
    }

    private SecretKey getKey(String secret) {
        byte[] secretBytes = Decoders.BASE64URL.decode(secret);
        return Keys.hmacShaKeyFor(secretBytes);
    }

}
