package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.request.UserRequest;
import co.com.pragma.api.dto.response.UserResponse;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.UserView;
import org.mapstruct.Mapper;
import utils.RoleTypes;

@Mapper(componentModel="spring")
public class UserApiMapper {
    public UserView toDomain(UserRequest userRequest) {
        return new UserView(
                userRequest.firstName(),
                userRequest.lastName(),
                userRequest.email(),
                userRequest.birthDate(),
                userRequest.idNumber(),
                userRequest.phone(),
                RoleTypes.CLIENT,
                userRequest.baseSalary(),
                userRequest.password()
        );
    }

    public UserResponse toResponse(UserView user) {
        return new UserResponse(null,
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getBirthDate(),
            user.getIdNumber(),
            user.getPhone(),
            user.getRole(),
            user.getBaseSalary(),
            user.getPassword()
        );
    }
}
