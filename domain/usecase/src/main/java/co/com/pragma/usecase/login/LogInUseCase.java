package co.com.pragma.usecase.login;

import co.com.pragma.model.token.Token;
import co.com.pragma.model.token.gateways.PasswordEncoderGateway;
import co.com.pragma.model.token.gateways.TokenRepository;
import co.com.pragma.model.user.gateways.UserRepository;
import exception.BusinessRuleViolatedException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LogInUseCase implements ILogInUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoderGateway passwordEncoder;
    private final TokenRepository tokenRepository;

    public Mono<Token> login(String email, String password) {

        return userRepository.findWithIdByEmail(email)
                .switchIfEmpty(Mono.error(new BusinessRuleViolatedException("bad credentials")))
                .flatMap(user -> passwordEncoder.matches(password, user.user().getPassword())
                        ? tokenRepository.generate(user)
                        : Mono.error(new BusinessRuleViolatedException("bad credentials")));
    }

}
