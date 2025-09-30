package com.cadt.portal.dto.request;

public record AuthResponse(
        String accessToken,
        String tokenType,
        long expiresInSeconds
) {
    public static AuthResponse bearer(String token, long expires) {
        return new AuthResponse(token, "Bearer", expires);
    }
}
