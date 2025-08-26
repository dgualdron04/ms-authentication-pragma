package co.com.pragma.usecase.user.validation;

import reactor.core.publisher.Mono;

public interface ReactiveValidator<T> {
    Mono<T> validate(T candidate);
}
