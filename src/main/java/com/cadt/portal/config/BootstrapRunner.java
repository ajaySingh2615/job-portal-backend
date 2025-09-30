package com.cadt.portal.config;

import com.cadt.portal.model.Role;
import com.cadt.portal.model.User;
import com.cadt.portal.respository.RoleRepository;
import com.cadt.portal.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class BootstrapRunner implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        ensureRole("ADMIN");
        ensureRole("RECRUITER");
        ensureRole("CANDIDATE");

        // Ensure admin user
        String adminEmail = "admin@jobportal.local";
        User admin = userRepository.findByEmail(adminEmail).orElse(null);
        if (admin == null) {
            Role adminRole = roleRepository.findByName("ADMIN").orElseThrow();
            admin = User.builder()
                    .email(adminEmail)
                    .fullName("System Admin")
                    .password(passwordEncoder.encode("Admin@123")) // change in prod!
                    .enabled(true)
                    .build();
            admin.setRoles(Set.of(adminRole));
            userRepository.save(admin);
        } else {
            // If seeded by Flyway with 'temp', upgrade to BCrypt
            if (!admin.getPassword().startsWith("$2a$") && !admin.getPassword().startsWith("$2b$")
                    && !admin.getPassword().startsWith("$2y$")) {
                admin.setPassword(passwordEncoder.encode("Admin@123"));
                userRepository.save(admin);
            }
        }
    }

    private void ensureRole(String name) {
        roleRepository.findByName(name).orElseGet(() -> roleRepository.save(Role.builder().name(name).build()));
    }
}
