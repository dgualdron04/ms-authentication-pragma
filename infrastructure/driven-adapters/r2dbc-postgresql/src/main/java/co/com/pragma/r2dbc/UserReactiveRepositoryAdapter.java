package co.com.pragma.r2dbc;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.r2dbc.entity.UserEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class UserReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    User,
    UserEntity,
    UUID,
    UserReactiveRepository
> implements UserRepository {
        public UserReactiveRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper) {
            super(repository, mapper, entity -> mapper.map(entity, User.class));
        }

        @Override
        public Mono<User> save(User user) {
            return super.save(user);
        }

        @Override
        public Flux<User> findAll() {
            return super.findAll();
        }

        @Override
        public Mono<Boolean> existsByEmail(String email) {
            return repository.existsByEmail(email);
        }

        @Override
        public Mono<Boolean> existsByIdNumber(Long idNumber) { return repository.existsByIdNumber(idNumber); }
}
