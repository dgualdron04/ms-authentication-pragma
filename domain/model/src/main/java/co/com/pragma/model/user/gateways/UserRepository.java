package co.com.pragma.model.user.gateways;

import co.com.pragma.model.token.Token;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.UserWithId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> save(User user);
    Flux<User> findAll();
    Mono<Boolean> existsByEmail(String email);
    Mono<Boolean> existsByIdNumber(Long idNumber);
    Mono<Token> login(String email, String password);
    Mono<User> findByEmail(String email);
    Mono<UserWithId> findWithIdByEmail(String email);
}
