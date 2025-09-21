package co.com.pragma.usecase.user;

import co.com.pragma.model.user.UserFilter;
import co.com.pragma.model.user.UserSearchFilters;
import co.com.pragma.model.user.UserView;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import utils.pagination.PageOptions;
import utils.pagination.PageResult;

public interface IUserUseCase {
    Mono<UserView> save(UserView user);
    Mono<Boolean> existsByEmail(String email);
    Mono<Boolean> existsByIdNumber(Long idNumber);
    Flux<UserFilter> findUsers(UserSearchFilters userSearchFilters);
    Mono<PageResult<UserFilter>> findUsersPaged(UserSearchFilters userSearchFilters, PageOptions pageOptions);
}
