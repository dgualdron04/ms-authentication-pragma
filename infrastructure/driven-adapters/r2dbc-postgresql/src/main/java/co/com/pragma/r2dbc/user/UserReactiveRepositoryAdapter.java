package co.com.pragma.r2dbc.user;

import co.com.pragma.model.token.Token;
import co.com.pragma.model.user.*;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.r2dbc.entity.UserEntity;
import co.com.pragma.r2dbc.entity.UserWithRolesView;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import co.com.pragma.r2dbc.user.view.UserViewReactiveRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import utils.RoleTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class UserReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    User,
    UserEntity,
    UUID,
    UserReactiveRepository
> implements UserRepository {

    public UserReactiveRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper, UserViewReactiveRepository viewRepo, R2dbcEntityTemplate template) {
        super(repository, mapper, entity -> mapper.map(entity, User.class));
        this.viewRepo = viewRepo;
        this.template = template;
    }

    private final UserViewReactiveRepository viewRepo;
    private final R2dbcEntityTemplate template;

    @Override
    public Mono<User> save(User user) {
        return super.save(user);
    }

    @Override
    public Flux<UserFilter> getAllWithRoleType() {
        return viewRepo.findAll()
                .map(u -> new UserFilter(
                u.getFirstName(),
                u.getLastName(),
                u.getEmail(),
                u.getBirthDate(),
                u.getIdNumber(),
                u.getPhone(),
                RoleTypes.fromName(u.getRoleName()),
                u.getBaseSalary()
        ));
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Mono<Boolean> existsByIdNumber(Long idNumber) { return repository.existsByIdNumber(idNumber); }

    @Override
    public Mono<Token> login(String email, String password) {
        return null;
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(e -> mapper.map(e, User.class));
    }

    @Override
    public Mono<UserWithId> findWithIdByEmail(String email) {
        return repository.findByEmail(email)
                .map(e -> new UserWithId(
                        e.getId(),
                        mapper.map(e, User.class)
                ));
    }

    @Override
    public Flux<UserFilter> search(UserSearchFilters userSearchFilters) {
        Criteria c = buildCriteria(userSearchFilters);
        Query q = Query.query(c);
        return template.select(q, UserWithRolesView.class)
                .map(u -> new UserFilter(
                        u.getFirstName(),
                        u.getLastName(),
                        u.getEmail(),
                        u.getBirthDate(),
                        u.getIdNumber(),
                        u.getPhone(),
                        RoleTypes.fromName(u.getRoleName()),
                        u.getBaseSalary()
                ));
    }

    private Criteria buildCriteria(UserSearchFilters u) {
        List<Criteria> list = new ArrayList<>();
        if (u.firstName() != null && !u.firstName().isEmpty())
            list.add(Criteria.where("firstname").like("%" + u.firstName().trim() + "%").ignoreCase(true));
        if (u.lastName() != null && !u.lastName().isEmpty())
            list.add(Criteria.where("lastname").like("%" + u.lastName().trim() + "%").ignoreCase(true));
        if (u.email() != null && !u.email().isEmpty())
            list.add(Criteria.where("email").like("%" + u.email().trim() + "%").ignoreCase(true));
        if (u.phone() != null && !u.phone().isEmpty())
            list.add(Criteria.where("phone").like("%" + u.phone().trim() + "%"));
        if (u.idNumber() != null && !u.idNumber().isEmpty())
            list.add(Criteria.where("idnumber").is(u.idNumber().trim()));
        if (u.roleName() != null)
            list.add(Criteria.where("rolename").is(u.roleName()));
        if (u.birthDateFrom() != null)
            list.add(Criteria.where("birthdate").greaterThanOrEquals(u.birthDateFrom()));
        if (u.birthDateTo() != null)
            list.add(Criteria.where("birthdate").lessThanOrEquals(u.birthDateTo()));
        if (u.minBaseSalary() != null && u.minBaseSalary() > 0)
            list.add(Criteria.where("basesalary").greaterThanOrEquals(u.minBaseSalary()));
        if (u.maxBaseSalary() != null && u.maxBaseSalary() > 0)
            list.add(Criteria.where("basesalary").lessThanOrEquals(u.maxBaseSalary()));
        return list.isEmpty() ? Criteria.empty() : Criteria.from(list.toArray(new Criteria[0]));
    }
}
