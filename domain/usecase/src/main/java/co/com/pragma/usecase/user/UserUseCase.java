package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.validation.ReactiveValidator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class UserUseCase implements IUserUseCase {

    private final UserRepository userRepository;
    private final List<ReactiveValidator<User>> validators;

    public Mono<User> saveUser(User user) {
        Mono<User> validated = Flux.fromIterable(validators)
                .reduce(Mono.just(user), (mono, v) -> mono.flatMap(v::validate))
                .flatMap(m -> m); // unwrap Mono<Mono<User>> -> Mono<User>
        return validated.flatMap(userRepository::save);
    }

    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }
}
