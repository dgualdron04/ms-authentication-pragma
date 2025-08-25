package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.request.UserRequest;
import co.com.pragma.api.dto.response.UserResponse;
import co.com.pragma.model.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public class UserApiMapper {
    public User toDomain(UserRequest userRequest) {
        return new User(null,
                userRequest.firstName(),
                userRequest.lastName(),
                userRequest.email(),
                userRequest.birthDate(),
                userRequest.idNumber(),
                userRequest.phone(),
                userRequest.baseSalary()
        );
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getBirthDate(),
            user.getIdNumber(),
            user.getPhone(),
            user.getBaseSalary()
        );
    }
}
