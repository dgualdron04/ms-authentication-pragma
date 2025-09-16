package co.com.pragma.api.security.jwt.manager;

import co.com.pragma.api.security.exception.SecurityAppException;
import co.com.pragma.api.security.jwt.provider.JwtProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import utils.ErrorTypes;

import io.jsonwebtoken.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtProvider jwtProvider;

    public JwtAuthenticationManager(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials() == null
                ? null
                : authentication.getCredentials().toString();

        if (token == null || token.isBlank()) {
            return Mono.error(new SecurityAppException(ErrorTypes.TOKEN_MISSING));
        }

        return Mono.fromCallable(() -> jwtProvider.getClaims(token))
                .onErrorMap(ExpiredJwtException.class,
                        ex -> new SecurityAppException(ErrorTypes.TOKEN_EXPIRED, ex))
                .onErrorMap(SignatureException.class,
                        ex -> new SecurityAppException(ErrorTypes.TOKEN_SIGNATURE_INVALID, ex))
                .onErrorMap(MalformedJwtException.class,
                        ex -> new SecurityAppException(ErrorTypes.TOKEN_INVALID, ex))
                .onErrorMap(UnsupportedJwtException.class,
                        ex -> new SecurityAppException(ErrorTypes.TOKEN_UNSUPPORTED, ex))
                .onErrorMap(IllegalArgumentException.class,
                        ex -> new SecurityAppException(ErrorTypes.TOKEN_INVALID, ex))
                .onErrorMap(ex -> !(ex instanceof SecurityAppException),
                        ex -> new SecurityAppException(ErrorTypes.AUTHENTICATION_FAILED, ex))

                .map(claims -> {
                    List<GrantedAuthority> authorities = new ArrayList<>();

                    Object roleClaim = claims.get("role");
                    if (roleClaim instanceof List<?> list) {
                        for (Object o : list) {
                            if (o instanceof String s) {
                                authorities.add(new SimpleGrantedAuthority(s));
                            }
                        }
                    }


                    return new UsernamePasswordAuthenticationToken(
                            claims.getSubject(),
                            null,
                            authorities
                    );
                });
    }

}
