package co.com.pragma.usecase.user.validation;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.UserView;
import reactor.core.publisher.Mono;
import exception.BusinessRuleViolatedException;


public class RequiredFieldsValidator implements ReactiveValidator<UserView> {
    @Override
    public Mono<UserView> validate(UserView u) {
        if (isBlank(u.getFirstName()))
            return Mono.error(new BusinessRuleViolatedException("The First Name field is required."));
        if (isBlank(u.getLastName()))
            return Mono.error(new BusinessRuleViolatedException("The Last Name field is required."));
        if (u.getBaseSalary() == null)
            return Mono.error(new BusinessRuleViolatedException("The base Salary field is required."));
        return Mono.just(u);
    }

    private boolean isBlank(String s){ return s == null || s.trim().isEmpty(); }
}
