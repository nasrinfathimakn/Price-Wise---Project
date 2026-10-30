package com.pricewise.config;

import com.pricewise.entity.User;
import com.pricewise.enums.Role;
import com.pricewise.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        ensureUser("admin@pricewise.local", "Administrator", Role.ADMIN, "Password@123");
        ensureUser("john.doe@example.com", "John Doe", Role.USER, "Password@123");
    }

    private void ensureUser(String email, String name, Role role, String rawPassword) {
        userRepository.findByEmail(email).ifPresentOrElse(user -> {
            user.setPasswordHash(passwordEncoder.encode(rawPassword));
            user.setEnabled(true);
            user.setRole(role);
            userRepository.save(user);
            log.info("Initialized verified BCrypt credentials for {} ({})", email, role);
        }, () -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setRole(role);
            newUser.setPasswordHash(passwordEncoder.encode(rawPassword));
            newUser.setEnabled(true);
            userRepository.save(newUser);
            log.info("Created user with verified BCrypt credentials for {} ({})", email, role);
        });
    }
}

