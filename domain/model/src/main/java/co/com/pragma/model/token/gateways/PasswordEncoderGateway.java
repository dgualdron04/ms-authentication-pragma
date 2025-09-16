package co.com.pragma.model.token.gateways;

public interface PasswordEncoderGateway {
    boolean matches(String rawPassword, String encodedPassword);
    String encode(String rawPassword);
}
