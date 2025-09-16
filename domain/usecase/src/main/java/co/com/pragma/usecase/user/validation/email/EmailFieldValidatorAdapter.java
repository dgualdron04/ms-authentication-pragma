package co.com.pragma.usecase.user.validation.email;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.UserView;
import co.com.pragma.usecase.user.validation.ReactiveValidator;
import reactor.core.publisher.Mono;

public class EmailFieldValidatorAdapter implements ReactiveValidator<UserView> {
    private final ReactiveValidator<String> emailStringValidator;

    public EmailFieldValidatorAdapter(ReactiveValidator<String> emailStringValidator) {
        this.emailStringValidator = emailStringValidator;
    }

    @Override
    public Mono<UserView> validate(UserView u) {
        return emailStringValidator.validate(u.getEmail()).thenReturn(u);
    }
}
