package eodigatji.eodigatjiserver.auth.security;

public record AuthenticatedUser(
        Long userId,
        String email
) {
}
