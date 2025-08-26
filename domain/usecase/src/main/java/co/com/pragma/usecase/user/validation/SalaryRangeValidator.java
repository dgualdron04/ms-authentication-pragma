package co.com.pragma.usecase.user.validation;

import co.com.pragma.model.user.User;
import reactor.core.publisher.Mono;
import exception.BusinessRuleViolatedException;

public class SalaryRangeValidator implements ReactiveValidator<User> {
    @Override
    public Mono<User> validate(User u) {
        System.out.println(u);
        int s = u.getBaseSalary();
        if (s < 0 || s > 15_000_000) {
            return Mono.error(new BusinessRuleViolatedException("The Base Salary should be between 0 and 15000000."));
        }
        return Mono.just(u);
    }
}
