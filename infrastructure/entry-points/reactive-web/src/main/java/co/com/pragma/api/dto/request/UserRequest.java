package co.com.pragma.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

@Schema(name = "UserRequest")
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
