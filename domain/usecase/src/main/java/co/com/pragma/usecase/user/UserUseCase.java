package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.validation.ReactiveValidator;
import gateways.CustomLogger;
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
    private final CustomLogger logger;

    public Mono<User> saveUser(User user) {
        logger.info("SaveUser: start email={}", user.getEmail());
        Mono<User> pipeline = Mono.just(user)
                .doOnSubscribe(
                        sub -> logger.trace("Validation pipeline started for email={}", user.getEmail()
                        )
                );
        for (ReactiveValidator<User> v : validators) {
            pipeline = pipeline
                    .doOnNext(u -> logger.trace("Running validator {}", v.getClass().getSimpleName()))
                    .flatMap(v::validate)
                    .doOnError(e -> logger.warn("Validation failed in {}: {}", v.getClass().getSimpleName(), e.toString()));
        }

        return transactionalGateway.executeTransactional(
                pipeline.flatMap(userRepository::save)
                        .doOnSubscribe(sub -> logger.debug("Persisting user email = {}", user.getEmail()))
                        .doOnSuccess(saved -> logger.info("User persisted succesfully - id = {}", saved.getId()))
                        .doOnError(e -> logger.warn("Error persisting user email = {}", user.getEmail(), e))
        );
    }

    public Flux<User> getAllUsers() {
        logger.info("getAllUsers: find all users.");
        return userRepository.findAll();
    }
}
