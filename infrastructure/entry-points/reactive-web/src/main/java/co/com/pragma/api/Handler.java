package co.com.pragma.api;

import co.com.pragma.api.dto.request.LogInDTO;
import co.com.pragma.api.dto.request.TokenDTO;
import co.com.pragma.api.dto.request.UserRequest;
import co.com.pragma.api.exception.model.InternalException;
import co.com.pragma.api.mapper.UserApiMapper;
import co.com.pragma.model.user.UserFilter;
import co.com.pragma.model.user.UserSearchFilters;
import co.com.pragma.usecase.login.ILogInUseCase;
import co.com.pragma.usecase.user.IUserUseCase;
import exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import utils.pagination.PageOptions;
import utils.pagination.PageResult;
import utils.pagination.SortOrder;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class wwwwHandler {

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

    @PreAuthorize("hasAnyAuthority('ADVISOR')")
    public Mono<ServerResponse> listenGetAllUsers(ServerRequest serverRequest) {
        UserSearchFilters filters = searchFilters(serverRequest);

        boolean existPaging = serverRequest.queryParam("page").isPresent()
                || serverRequest.queryParam("size").isPresent()
                || serverRequest.queryParam("sort").isPresent();

        if (!existPaging) {
            return ServerResponse.ok()
                    .contentType(MediaType.TEXT_EVENT_STREAM)
                    .body(userUseCase.findUsers(filters), UserFilter.class);
        }

        int page = serverRequest.queryParam("page").map(Integer::parseInt).orElse(0);
        int size = serverRequest.queryParam("size").map(Integer::parseInt).orElse(20);

        if (page <= 0) page = 0;
        if (size <= 0) size = 20;
        if (size > 100) size = 100;

        List<SortOrder> sort = serverRequest.queryParams().getOrDefault("sort", List.of())
                .stream()
                .map(s -> {
                    String[] p = s.split(",", 2);
                    String field = p[0].trim();
                    boolean asc = p.length < 2 || !"desc".equalsIgnoreCase(p[1].trim());
                    return new SortOrder(field, asc);
                })
                .toList();

        PageOptions pageOptions = new PageOptions(page, size, sort);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userUseCase.findUsersPaged(filters, pageOptions), PageResult.class);
    }

    private UserSearchFilters searchFilters(ServerRequest serverRequest) {
        return new UserSearchFilters(
                serverRequest.queryParam("firstName").orElse(null),
                serverRequest.queryParam("lastName").orElse(null),
                serverRequest.queryParam("email").orElse(null),
                serverRequest.queryParam("birthDateFrom").map(java.time.LocalDate::parse).orElse(null),
                serverRequest.queryParam("birthDateTo").map(java.time.LocalDate::parse).orElse(null),
                serverRequest.queryParam("idNumber").orElse(null),
                serverRequest.queryParam("phone").orElse(null),
                serverRequest.queryParam("roleName").orElse(null),
                serverRequest.queryParam("minBaseSalary").map(Integer::valueOf).orElse(null),
                serverRequest.queryParam("maxBaseSalary").map(Integer::valueOf).orElse(null)
        );
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
