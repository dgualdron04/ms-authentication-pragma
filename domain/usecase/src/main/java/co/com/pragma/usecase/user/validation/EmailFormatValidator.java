package co.com.pragma.usecase.user.validation;


import co.com.pragma.model.user.User;
import reactor.core.publisher.Mono;
import exception.BusinessRuleViolatedException;

import java.util.regex.Pattern;

public class EmailFormatValidator implements ReactiveValidator<User> {
    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    @Override
    public Mono<User> validate(User u) {
        String e = u.getEmail();
        if (e == null || e.isBlank()) {
            return Mono.error(new BusinessRuleViolatedException("The email address is required."));
        } else if (!EMAIL.matcher(e).matches()) {
            return Mono.error(new BusinessRuleViolatedException("The email address is not valid."));
        }
        return Mono.just(u);
    }
}
