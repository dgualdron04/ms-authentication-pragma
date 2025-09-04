package co.com.pragma.model.user;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private String firstName;

    private String lastName;

    private String email;

    private LocalDate birthDate;

    private String idNumber;

    private String phone;

//    private Long rolId;

    private Integer baseSalary;
}
