package co.com.pragma.api.security.adapter;

import co.com.pragma.api.security.jwt.provider.JwtProvider;
import co.com.pragma.model.role.Role;
import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.model.token.Token;
import co.com.pragma.model.token.gateways.TokenRepository;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.UserWithId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenAdapter implements TokenRepository {

    private final JwtProvider tokenProvider;
    private final RoleRepository roleRepository;

    @Override
    public Mono<Token> generate(UserWithId userWithId) {
        User user = userWithId.user();
        return roleRepository.findById(user.getRoleId()).map(List::of)
                .map(roles -> tokenProvider.generateToken(
                        userWithId.id().toString(),
                        user.getEmail(),
                        roles
                ))
                .map(jwt -> Token.builder().token(jwt).build());
    }

}
