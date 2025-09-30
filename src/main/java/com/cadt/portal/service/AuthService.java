package com.cadt.portal.service;

import com.cadt.portal.dto.request.AuthLoginRequest;
import com.cadt.portal.dto.request.AuthRegisterRequest;
import com.cadt.portal.dto.response.AuthResponse;
import com.cadt.portal.dto.response.UserResponse;

public interface AuthService {
    AuthResponse register(AuthRegisterRequest req);

    AuthResponse login(AuthLoginRequest req);

    UserResponse me(String email);
}
