package co.com.pragma.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import utils.RoleTypes;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserView {
    private String firstName;

    private String lastName;

    private String email;

    private LocalDate birthDate;

    private String idNumber;

    private String phone;

    private RoleTypes role;

    private Integer baseSalary;

    private String password;
}
