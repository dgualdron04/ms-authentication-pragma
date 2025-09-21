package co.com.pragma.model.user.gateways;

import co.com.pragma.model.token.Token;
import co.com.pragma.model.user.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import utils.pagination.PageOptions;
import utils.pagination.PageResult;

public interface UserRepository {
    Mono<User> save(User user);
    Mono<Boolean> existsByEmail(String email);
    Mono<Boolean> existsByIdNumber(Long idNumber);
    Mono<Token> login(String email, String password);
    Mono<User> findByEmail(String email);
    Mono<UserWithId> findWithIdByEmail(String email);
    Flux<UserFilter> search(UserSearchFilters userView);
    Flux<UserFilter> getAllWithRoleType();
    Mono<PageResult<UserFilter>> searchPaged(UserSearchFilters userSearchFilters, PageOptions pageOptions);
    Mono<PageResult<UserFilter>> getAllWithRoleTypePaged(PageOptions pageOptions);
}
