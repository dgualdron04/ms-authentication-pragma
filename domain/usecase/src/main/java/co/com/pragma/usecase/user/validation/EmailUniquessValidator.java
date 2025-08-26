package co.com.pragma.usecase.user.validation;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import exception.AlreadyExistsException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class EmailUniquessValidator implements ReactiveValidator<User> {

    private final UserRepository userRepository;

    @Override
    public Mono<User> validate(User u) {
        return userRepository.existsByEmail(u.getEmail())
                .flatMap(exists -> exists
                        ? Mono.error(new AlreadyExistsException("The email is already registered."))
                        : Mono.just(u));
    }
}
