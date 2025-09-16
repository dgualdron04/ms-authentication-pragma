package co.com.pragma.r2dbc.role;

import co.com.pragma.model.role.Role;
import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.r2dbc.entity.RoleEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import utils.RoleTypes;

import java.util.UUID;

@Repository
public class RoleReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Role,
        RoleEntity,
        UUID,
        RoleReactiveRepository
> implements RoleRepository {
    public RoleReactiveRepositoryAdapter(RoleReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, entity -> mapper.map(entity, Role.class));
    }

    @Override
    public Mono<UUID> findIdByName(RoleTypes role) {
        return repository.findByName(role.getName())
                .map(RoleEntity::getId);
    }

    @Override
    public Mono<Role> findById(UUID id) {
        return repository.findById(id)
                .map(entity -> new Role(
                        RoleTypes.fromName(entity.getName()),
                        entity.getDescription()
                ));
    }
}
