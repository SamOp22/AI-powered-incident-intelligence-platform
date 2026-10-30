package com.halo.incident_service.user.bootstrap;

import com.halo.incident_service.user.entity.Role;
import com.halo.incident_service.user.repository.UserRepository;
import com.halo.incident_service.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminBootstrapRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminBootstrapRunner.class);

    private final UserRepository userRepository;
    private final UserService userService;
    private final String email;
    private final String password;

    public AdminBootstrapRunner(UserRepository userRepository, UserService userService,
                                @Value("${app.bootstrap-admin.email:}") String email,
                                @Value("${app.bootstrap-admin.password:}") String password) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.email = email;
        this.password = password;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.existsByRole(Role.ADMIN)) {
            return;
        }
        if (email.isBlank() || password.isBlank()) {
            log.warn("No ADMIN exists and no bootstrap credentials configured");
            return;
        }
        userService.register("Administrator", email, password, Role.ADMIN);
        log.info("Bootstrap admin created: {}", email);
    }
}
