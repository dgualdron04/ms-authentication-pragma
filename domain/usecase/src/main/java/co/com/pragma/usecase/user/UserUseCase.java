package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.validation.ReactiveValidator;
import co.com.pragma.usecase.user.validation.email.EmailStringValidator;
import exception.BusinessRuleViolatedException;
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
                        .doOnSuccess(saved -> logger.info("User persisted succesfully - idNumber = {}", saved.getIdNumber()))
                        .doOnError(e -> logger.warn("Error persisting user email = {}", user.getEmail(), e))
        );
    }

    public Flux<User> getAllUsers() {
        logger.info("getAllUsers: find all users.");
        return userRepository.findAll();
    }

    public Mono<Boolean> existsByEmail(String email) {
        logger.info("existsByEmail: email={}", email);

        return Mono.justOrEmpty(email)
                .switchIfEmpty(Mono.error(new BusinessRuleViolatedException("The email address is required.")))
                .doOnSubscribe(s -> logger.trace("Validation pipeline started for email = {}", email))
                .flatMap(new EmailStringValidator()::validate)
                .flatMap(userRepository::existsByEmail)
                .doOnNext(exists -> logger.info("existsByEmail result = {}", exists));
    }

    public Mono<Boolean> existsByIdNumber(Long idNumber) {
        logger.info("existsByIdNumber: idNumber={}", idNumber);

        return Mono.justOrEmpty(idNumber)
                .switchIfEmpty(Mono.error(new BusinessRuleViolatedException("The Id Number is required.")))
                .doOnSubscribe(s -> logger.trace("Validation pipeline started for id number = {}", idNumber))
                .flatMap(userRepository::existsByIdNumber)
                .doOnNext(exists -> logger.info("existsByIdNumber result = {}", exists));
    }
}
