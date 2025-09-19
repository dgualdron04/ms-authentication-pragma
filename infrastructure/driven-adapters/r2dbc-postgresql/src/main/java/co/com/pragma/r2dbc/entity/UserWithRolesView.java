package co.com.pragma.r2dbc.entity;

import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@Getter
@Setter
@Table("users_with_roles")
public class UserWithRolesView {
    @Column("firstname")
    private String firstName;

    @Column("lastname")
    private String lastName;

    @Column("email")
    private String email;

    @Column("birthdate")
    private LocalDate birthDate;

    @Column("idnumber")
    private String idNumber;

    @Column("phone")
    private String phone;

    @Column("rolename")
    private String roleName;

    @Column("basesalary")
    private Integer baseSalary;
}
