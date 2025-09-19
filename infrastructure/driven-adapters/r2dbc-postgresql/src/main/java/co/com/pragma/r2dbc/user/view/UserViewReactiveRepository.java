package co.com.pragma.r2dbc.user.view;

import co.com.pragma.model.user.UserWithId;
import co.com.pragma.r2dbc.entity.UserEntity;
import co.com.pragma.r2dbc.entity.UserWithRolesView;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

// TODO: This file is just an example, you should delete or modify it
public interface UserViewReactiveRepository extends ReactiveCrudRepository<UserWithRolesView, Void> {
    Flux<UserWithRolesView> findAll();
}
