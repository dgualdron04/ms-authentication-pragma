package co.com.pragma.api.dto.response;

import utils.RoleTypes;

import java.time.LocalDate;
import java.util.UUID;

public record UserResponse(UUID id,
                           String firstName,
                           String lastName,
                           String email,
                           LocalDate birthDate,
                           String idNumber,
                           String phone,
                           RoleTypes role,
                           int baseSalary,
                           String password
) {
};
