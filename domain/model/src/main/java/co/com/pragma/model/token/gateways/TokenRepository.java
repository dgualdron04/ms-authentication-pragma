package co.com.pragma.model.token.gateways;

import co.com.pragma.model.token.Token;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.UserWithId;
import reactor.core.publisher.Mono;

public interface TokenRepository {
    Mono<Token> generate(UserWithId user);
}
