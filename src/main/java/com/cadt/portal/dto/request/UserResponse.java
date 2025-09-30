package com.cadt.portal.dto.request;

import java.util.Set;

public record UserResponse(
        Long id,
        String fullName,
        String email,
        Set<String> roles
) {
}
