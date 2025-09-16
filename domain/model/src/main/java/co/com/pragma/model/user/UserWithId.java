package co.com.pragma.model.user;

import java.util.UUID;

public record UserWithId (UUID id, User user) {}
