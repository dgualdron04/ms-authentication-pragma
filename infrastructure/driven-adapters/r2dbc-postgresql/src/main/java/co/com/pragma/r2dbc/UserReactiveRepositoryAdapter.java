package co.com.pragma.r2dbc;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.r2dbc.entity.UserEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class UserReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    User/* change for domain model */,
    UserEntity/* change for adapter model */,
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
        public Mono<User> findById(UUID id) {
            return super.findById(id);
        }

        @Override
        public Mono<Void> deleteById(UUID id) {
            return repository.deleteById(id);
        }

/*{
    public UserReactiveRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper) {
        *//**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         *//*
        super(repository, mapper, d -> mapper.map(d, Object.class*//* change for domain model *//*));
    }*/

}
