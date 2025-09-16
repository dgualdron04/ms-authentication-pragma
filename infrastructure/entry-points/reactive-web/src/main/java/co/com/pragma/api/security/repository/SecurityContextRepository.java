package co.com.pragma.api.security.repository;


import co.com.pragma.api.security.exception.SecurityAppException;
import co.com.pragma.api.security.jwt.manager.JwtAuthenticationManager;
import jakarta.xml.bind.SchemaOutputResolver;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import utils.ErrorTypes;

@Component
@AllArgsConstructor
public class SecurityContextRepository implements ServerSecurityContextRepository {

    private final JwtAuthenticationManager jwtAuthenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        String auth = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (auth == null || !auth.startsWith("Bearer ")) {
            exchange.getAttributes().put("auth_error_type", ErrorTypes.TOKEN_MISSING);
            return Mono.empty();
        }
        /*String token = exchange.getAttribute("token");

        if (token == null || token.isBlank()) {
            return Mono.empty();
        }*/
        /*if (token == null || token.isBlank()) {
            return Mono.error(new SecurityAppException(ErrorTypes.TOKEN_MISSING));
        }*/

//        if (token == null) {
//            String auth = exchange.getRequest().getHeaders().getFirst("Authorization");
//            if (auth != null && auth.startsWith("Bearer ")) {
//                token = auth.substring(7);
//            }
//        }
//
//        if (token == null || token.isBlank()) {
//            return Mono.empty();
//        }
        String token = auth.substring(7);
        if (token.isBlank()) {
            exchange.getAttributes().put("auth_error_type", ErrorTypes.TOKEN_MISSING);
            return Mono.empty();
        }
        return jwtAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(token, token))
                .map(SecurityContextImpl::new)
                .onErrorResume(SecurityAppException.class, e -> {
                    exchange.getAttributes().put("auth_error_type", e.getErrorType());
                    return Mono.empty();
                })
                .cast(SecurityContext.class);
    }

}
