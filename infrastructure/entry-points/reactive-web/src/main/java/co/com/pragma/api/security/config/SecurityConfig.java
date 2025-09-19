package co.com.pragma.api.security.config;

import co.com.pragma.api.config.UserPath;
import co.com.pragma.api.security.repository.SecurityContextRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import utils.ErrorTypes;

import java.time.Instant;
import java.util.Map;


@AllArgsConstructor
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final SecurityContextRepository securityContextRepository;
    private final UserPath userPath;
    private final ObjectMapper objectMapper;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchangeSpec ->
                        exchangeSpec.pathMatchers(
                                userPath.getLogin(),
                                userPath.getUsers(),
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs/swagger-config",
                                "/webjars/**")
                            .permitAll()
                        .anyExchange().authenticated())
                .securityContextRepository(securityContextRepository)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .logout(ServerHttpSecurity.LogoutSpec::disable)
                .exceptionHandling(spec -> spec
                        .authenticationEntryPoint((exchange, ex) -> {
                            ErrorTypes type = exchange.getAttribute("auth_error_type");
                            if (type == null) type = ErrorTypes.AUTHENTICATION_FAILED;
                            return writeError(exchange, type);
                        })
                        .accessDeniedHandler((exchange, denied) ->
                                writeError(exchange, ErrorTypes.ACCESS_DENIED))
                )
                .build();
    }

    private Mono<Void> writeError(ServerWebExchange exchange, ErrorTypes type) {
        var res = exchange.getResponse();
        res.setStatusCode(HttpStatus.valueOf(type.getStatus()));
        res.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "timestamp", Instant.now().toString(),
                "path", exchange.getRequest().getPath().value(),
                "status", type.getStatus(),
                "errorCode", type.getErrorCode(),
                "title", type.getTitle(),
                "message", type.getMessage(),
                "errors", type.getErrors()
        );
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(body);
            return res.writeWith(Mono.just(res.bufferFactory().wrap(bytes)));
        } catch (Exception e) {
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return res.setComplete();
        }
    }
}
