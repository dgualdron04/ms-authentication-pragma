package co.com.pragma.model.role.gateways;

import co.com.pragma.model.role.Role;
import reactor.core.publisher.Mono;
import utils.RoleTypes;

import java.util.UUID;

public interface RoleRepository {
    Mono<UUID> findIdByName(RoleTypes role);
    Mono<Role> findById(UUID id);
}
