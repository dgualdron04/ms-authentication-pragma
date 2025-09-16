package co.com.pragma.usecase.login;

import co.com.pragma.model.token.Token;
import reactor.core.publisher.Mono;

public interface ILogInUseCase {
    Mono<Token> login(String email, String password);
}
