package co.com.pragma.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table("users")
public class UserEntity {
    @Id
    @Column("user_id")
    private UUID id;

    @Column("firstname")
    private String firstName;

    @Column("lastname")
    private String lastName;

    private String email;

    @Column("birthdate")
    private LocalDate birthDate;

    @Column("idnumber")
    private String idNumber;

    private String phone;

//    private Long rolId;
    @Column("basesalary")
    private int baseSalary;
}
