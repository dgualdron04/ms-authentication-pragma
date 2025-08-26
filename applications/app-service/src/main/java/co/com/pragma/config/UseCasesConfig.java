package co.com.pragma.config;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.IUserUseCase;
import co.com.pragma.usecase.user.UserUseCase;
import co.com.pragma.usecase.user.validation.*;
import gateways.TransactionalGateway;
import org.springframework.context.annotation.*;

import java.util.List;

@Configuration
@ComponentScan(basePackages = "co.com.pragma.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {
        @Primary
        @Bean(name = "userUseCaseCore")
        public IUserUseCase userUseCaseCore(UserRepository userRepository, TransactionalGateway transactionalGateway) {
                List<ReactiveValidator<User>> validators = List.of(
                        new RequiredFieldsValidator(),
                        new SalaryRangeValidator(),
                        new EmailFormatValidator(),
                        new EmailUniquessValidator(userRepository)
                );

                return new UserUseCase(userRepository, validators, transactionalGateway);
        }
}
