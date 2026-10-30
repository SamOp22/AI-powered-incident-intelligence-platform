package com.halo.incident_service.user.service;

import com.halo.incident_service.common.exception.ConflictException;
import com.halo.incident_service.user.entity.Role;
import com.halo.incident_service.user.entity.User;
import com.halo.incident_service.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;
    @InjectMocks UserService userService;

    @Test
    void register_normalizesEmailAndHashesPassword() {
        when(userRepository.existsByEmail("a@b.com")).thenReturn(false);
        when(passwordEncoder.encode("secret123")).thenReturn("hashed");
        when(userRepository.saveAndFlush(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User saved = userService.register("  Asha ", "  A@B.com ", "secret123", Role.ENGINEER);

        assertThat(saved.getEmail()).isEqualTo("a@b.com");
        assertThat(saved.getName()).isEqualTo("Asha");
        assertThat(saved.getPasswordHash()).isEqualTo("hashed");
        assertThat(saved.getRole()).isEqualTo(Role.ENGINEER);
    }

    @Test
    void register_duplicateEmail_throwsConflict() {
        when(userRepository.existsByEmail("a@b.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.register("Asha", "a@b.com", "secret123", Role.ENGINEER))
                .isInstanceOf(ConflictException.class);

        verify(userRepository, never()).saveAndFlush(any());
    }
}