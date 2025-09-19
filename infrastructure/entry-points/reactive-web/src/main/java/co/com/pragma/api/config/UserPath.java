package co.com.pragma.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "routes.paths")
public final class UserPath {
    private String users;
    private String usersById;
    private String existsByEmail;
    private String existsByIdNumber;
    private String login;
    private String hello;
    private List<String> swagger = new ArrayList<>();
}
