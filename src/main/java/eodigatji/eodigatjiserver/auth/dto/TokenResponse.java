package eodigatji.eodigatjiserver.auth.dto;

public record TokenResponse(
        String tokenType,
        String accessToken,
        String refreshToken
) {
    public static TokenResponse bearer(String accessToken, String refreshToken) {
        return new TokenResponse("Bearer", accessToken, refreshToken);
    }
}
