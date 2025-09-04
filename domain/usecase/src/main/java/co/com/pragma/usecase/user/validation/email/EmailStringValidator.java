package co.com.pragma.usecase.user.validation.email;


import co.com.pragma.usecase.user.validation.ReactiveValidator;
import reactor.core.publisher.Mono;
import exception.BusinessRuleViolatedException;

import java.util.regex.Pattern;

public class EmailStringValidator implements ReactiveValidator<String> {
    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    @Override
    public Mono<String> validate(String e) {

        if (e == null || e.isBlank()) {
            return Mono.error(new BusinessRuleViolatedException("The email address is required."));
        } else if (!EMAIL.matcher(e).matches()) {
            return Mono.error(new BusinessRuleViolatedException("The email address is not valid."));
        }
        return Mono.just(e);
    }
}
