package co.com.pragma.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "routes.paths")
public class UserPath {
    private String users;
    private String usersById;
    private String existsByEmail;
    private String existsByIdNumber;
}
