package utils;

import exception.BusinessRuleViolatedException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum RoleTypes {

    CLIENT("Cliente"),
    ADVISOR("Asesor"),
    ADMINISTRATOR("Administrador"),
    SERVICE("Servicio");

    private final String name;

    public static RoleTypes fromName(String name) {
        return Arrays.stream(values())
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new BusinessRuleViolatedException("The role does not exist: " + name));
    }
}
