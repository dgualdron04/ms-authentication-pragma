package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.validation.ReactiveValidator;
import gateways.TransactionalGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class UserUseCase implements IUserUseCase {

    private final UserRepository userRepository;
    private final List<ReactiveValidator<User>> validators;
    private final TransactionalGateway transactionalGateway;

    public Mono<User> saveUser(User user) {
        Mono<User> pipeline = Mono.just(user);
        for (ReactiveValidator<User> v : validators) {
            pipeline = pipeline.flatMap(v::validate);
        }
        return transactionalGateway.executeTransactional(
                pipeline.flatMap(userRepository::save)
        );
    }

    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }
}
