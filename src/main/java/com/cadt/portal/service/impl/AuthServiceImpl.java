package com.cadt.portal.service.impl;

import com.cadt.portal.dto.request.AuthLoginRequest;
import com.cadt.portal.dto.request.AuthRegisterRequest;
import com.cadt.portal.dto.response.AuthResponse;
import com.cadt.portal.dto.response.UserResponse;
import com.cadt.portal.model.Role;
import com.cadt.portal.model.User;
import com.cadt.portal.respository.RoleRepository;
import com.cadt.portal.respository.UserRepository;
import com.cadt.portal.security.JwtService;
import com.cadt.portal.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    @Override
    public AuthResponse register(AuthRegisterRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new IllegalArgumentException("Email already exists");
        }
        Role candidate = roleRepository.findByName("CANDIDATE")
                .orElseThrow(() -> new IllegalStateException("Role CANDIDATE missing"));

        User user = User.builder()
                .email(req.email().toLowerCase())
                .fullName(req.fullName())
                .password(passwordEncoder.encode(req.password()))
                .enabled(true)
                .build();
        user.getRoles().add(candidate);
        userRepository.save(user);

        String token = jwtService.generateToken(user.getId(), user.getEmail(), toRoleNames(user));
        return AuthResponse.bearer(token, jwtService.getExpirationSeconds());
    }

    @Override
    public AuthResponse login(AuthLoginRequest req) {
        Authentication auth = new UsernamePasswordAuthenticationToken(req.email().toLowerCase(), req.password());
        auth = authenticationManager.authenticate(auth);
        SecurityContextHolder.getContext().setAuthentication(auth);

        User user = userRepository.findByEmail(req.email().toLowerCase())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
        String token = jwtService.generateToken(user.getId(), user.getEmail(), toRoleNames(user));
        return AuthResponse.bearer(token, jwtService.getExpirationSeconds());
    }

    @Override
    public UserResponse me(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new UserResponse(user.getId(), user.getFullName(), user.getEmail(), toRoleNames(user));
    }

    private Set<String> toRoleNames(User user) {
        return user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
    }
}
