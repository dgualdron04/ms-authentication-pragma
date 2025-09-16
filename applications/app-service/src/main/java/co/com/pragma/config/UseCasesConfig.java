package co.com.pragma.config;

import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.model.token.gateways.PasswordEncoderGateway;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.UserView;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.IUserUseCase;
import co.com.pragma.usecase.user.UserUseCase;
import co.com.pragma.usecase.user.validation.*;
import co.com.pragma.usecase.user.validation.email.EmailFieldValidatorAdapter;
import co.com.pragma.usecase.user.validation.email.EmailStringValidator;
import co.com.pragma.usecase.user.validation.email.EmailUniquessValidator;
import gateways.CustomLogger;
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
        public IUserUseCase userUseCaseCore(UserRepository userRepository, RoleRepository roleRepository, TransactionalGateway transactionalGateway, CustomLogger customLogger, PasswordEncoderGateway passwordEncoder) {

                List<ReactiveValidator<UserView>> validators = List.of(
                        new RequiredFieldsValidator(),
                        new SalaryRangeValidator(),
                        new EmailFieldValidatorAdapter(new EmailStringValidator()),
                        new EmailUniquessValidator(userRepository)
                );

                return new UserUseCase(userRepository, roleRepository, validators, transactionalGateway, customLogger, passwordEncoder);
        }
}
