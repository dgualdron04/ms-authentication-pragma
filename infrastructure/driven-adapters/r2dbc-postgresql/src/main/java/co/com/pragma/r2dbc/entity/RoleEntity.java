package co.com.pragma.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table("roles")
public class RoleEntity {
    @Id
    @Column("role_id")
    private UUID id;

    @Column("name")
    private String name;

    @Column("description")
    private String description;
}
