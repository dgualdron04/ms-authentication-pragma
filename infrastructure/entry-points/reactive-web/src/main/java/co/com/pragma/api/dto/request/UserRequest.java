package co.com.pragma.api.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

public record UserRequest(
        String firstName,
        String lastName,
        String email,
        LocalDate birthDate,
        String idNumber,
        String phone,
        Integer baseSalary
) {
}
