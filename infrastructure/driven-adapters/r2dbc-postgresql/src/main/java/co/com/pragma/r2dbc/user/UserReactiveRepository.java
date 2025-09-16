package co.com.pragma.r2dbc.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.UserWithId;
import co.com.pragma.r2dbc.entity.UserEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

// TODO: This file is just an example, you should delete or modify it
public interface UserReactiveRepository extends ReactiveCrudRepository<UserEntity, UUID>, ReactiveQueryByExampleExecutor<UserEntity> {
    Mono<Boolean> existsByEmail(String email);
    Mono<Boolean> existsByIdNumber(Long idNumber);
    Mono<UserEntity> findByEmail(String email);
    Mono<UserWithId> findWithIdByEmail(String email);
}
