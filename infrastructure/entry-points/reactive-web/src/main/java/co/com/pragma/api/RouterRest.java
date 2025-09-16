package co.com.pragma.api;

import co.com.pragma.api.config.UserPath;
import co.com.pragma.api.docs.UserDocs;
import co.com.pragma.api.exception.GlobalErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;

@Configuration
@RequiredArgsConstructor
public class RouterRest {

    private final UserPath userPath;
    private final Handler userHandler;
    private final UserDocs userDocs;

    @Bean
    public RouterFunction<ServerResponse> routerFunction(GlobalErrorHandler globalErrorHandler) {
        var routes = route()
                .POST(userPath.getUsers(), req -> true, userHandler::listenSaveUser, userDocs.save())
                .GET (userPath.getExistsByEmail(), userHandler::existsByEmail, userDocs.existsByEmail())
                .GET (userPath.getExistsByIdNumber(), userHandler::existsByIdNumber, userDocs.existsByIdNumber())
                .GET(userPath.getHello(), userHandler::hello, userDocs.login())
                .POST(userPath.getLogin(), userHandler::logIn, userDocs.login())
                .build();

        return routes.filter(globalErrorHandler);

    }
}
