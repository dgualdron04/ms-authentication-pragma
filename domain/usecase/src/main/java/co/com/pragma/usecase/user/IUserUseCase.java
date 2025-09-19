package co.com.pragma.usecase.user;

import co.com.pragma.model.token.Token;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.UserFilter;
import co.com.pragma.model.user.UserSearchFilters;
import co.com.pragma.model.user.UserView;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUserUseCase {
    Mono<UserView> save(UserView user);
    Mono<Boolean> existsByEmail(String email);
    Mono<Boolean> existsByIdNumber(Long idNumber);
    Flux<UserFilter> findUsers(UserSearchFilters userSearchFilters);
}
