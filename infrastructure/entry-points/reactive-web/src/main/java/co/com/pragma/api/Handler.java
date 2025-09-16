package co.com.pragma.api;

import co.com.pragma.api.dto.request.LogInDTO;
import co.com.pragma.api.dto.request.TokenDTO;
import co.com.pragma.api.dto.request.UserRequest;
import co.com.pragma.api.exception.model.InternalException;
import co.com.pragma.api.mapper.UserApiMapper;
import co.com.pragma.model.user.User;
import co.com.pragma.usecase.login.ILogInUseCase;
import co.com.pragma.usecase.user.IUserUseCase;
import co.com.pragma.usecase.user.UserUseCase;
import exception.BusinessRuleViolatedException;
import exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.print.attribute.standard.Media;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Handler {

    private final IUserUseCase userUseCase;
    private final UserApiMapper userApiMapper;
    private final ILogInUseCase logInUseCase;

    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'ADVISOR')")
    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UserRequest.class)
                .map(userApiMapper::toDomain)
                .flatMap(userUseCase::save)
                .map(userApiMapper::toResponse)
                .flatMap(res -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(res))
                .onErrorResume(ex -> Mono.error(ex instanceof DomainException ? ex : new InternalException(ex, null)));
    }

    public Mono<ServerResponse> listenGetAllUsers(ServerRequest serverRequest) {
        return ServerResponse.ok()
                //.contentType(MediaType.APPLICATION_JSON)
                //.contentType(MediaType.APPLICATION_NDJSON)
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(userUseCase.getAllUsers(), User.class);
    }

    @PreAuthorize("hasAnyAuthority('SERVICE', 'CLIENT')")
    public Mono<ServerResponse> existsByEmail(ServerRequest serverRequest) {
        final String email = serverRequest.pathVariable("email");

        return userUseCase.existsByEmail(email)
                .flatMap(exists -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of("exists", exists)));
    }

    @PreAuthorize("hasAnyAuthority('SERVICE', 'CLIENT')")
    public Mono<ServerResponse> existsByIdNumber(ServerRequest serverRequest) {
        final Long idNumber = Long.parseLong(serverRequest.pathVariable("idNumber"));

        return userUseCase.existsByIdNumber(idNumber)
                .flatMap(exists -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of("exists", exists)));

    }

    @PreAuthorize("hasAuthority('CLIENT')")
    public Mono<ServerResponse> hello(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(Mono.just("Hello World!"), String.class);
    }

    public Mono<ServerResponse> logIn(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LogInDTO.class)
                .flatMap(dto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(logInUseCase.login(dto.email(), dto.password()), TokenDTO.class));
    }

}
