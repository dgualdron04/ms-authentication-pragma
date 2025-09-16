package co.com.pragma.model.role;
import lombok.*;
//import lombok.NoArgsConstructor;
import utils.RoleTypes;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Role {
    private RoleTypes name;
    private String description;
}
