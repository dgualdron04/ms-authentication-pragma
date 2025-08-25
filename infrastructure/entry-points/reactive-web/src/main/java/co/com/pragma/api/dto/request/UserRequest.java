package co.com.pragma.api.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

public record UserRequest(
        @NotBlank(message = "El nombre no puede estar vacío")
        String firstName,
        @NotBlank(message = "El apellido no puede estar vacío")
        String lastName,
        @NotBlank @Email(message = "El correo electrónico no tiene un formato válido")
        String email,
        @Past(message = "La fecha de nacimiento debe ser en el pasado")
        LocalDate birthDate,
        @NotBlank(message = "El documento de identidad no puede ser nulo")
        String idNumber,
        String phone,
        @NotNull(message = "El salario base no puede ser nulo")
        @PositiveOrZero(message = "El salario no puede ser negativo")
        @Max(value = 15_000_000, message = "El salario no puede superar 15'000.000")
        Integer baseSalary 
) {}
