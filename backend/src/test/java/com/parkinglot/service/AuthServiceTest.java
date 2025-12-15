package com.parkinglot.service;

import com.parkinglot.dto.AuthResponse;
import com.parkinglot.dto.LoginRequest;
import com.parkinglot.dto.RegisterRequest;
import com.parkinglot.model.User;
import com.parkinglot.repository.UserRepository;
import com.parkinglot.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private AuthenticationManager authenticationManager;

    private AuthService authService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthService(userRepository, passwordEncoder, jwtUtil, authenticationManager);
    }

    @Test
    void register_whenEmailExists_throws() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("a@b.com");
        when(userRepository.existsByEmail("a@b.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> authService.register(req));
    }

    @Test
    void register_success_returnsAuthResponse() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("a@b.com");
        req.setFirstName("First");
        req.setLastName("Last");
        req.setPassword("pass");

        when(userRepository.existsByEmail("a@b.com")).thenReturn(false);
        when(passwordEncoder.encode("pass")).thenReturn("encoded");

        User saved = new User();
        saved.setId(1L);
        saved.setEmail("a@b.com");
        saved.setFirstName("First");
        saved.setLastName("Last");
        when(userRepository.save(any())).thenReturn(saved);
        when(jwtUtil.generateToken("a@b.com")).thenReturn("token-123");

        AuthResponse resp = authService.register(req);
        assertThat(resp).isNotNull();
        assertThat(resp.getToken()).isEqualTo("token-123");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getPassword()).isEqualTo("encoded");
    }

    @Test
    void login_whenInvalid_throws() {
        LoginRequest req = new LoginRequest();
        req.setEmail("no@user.com");
        req.setPassword("x");

        doThrow(new RuntimeException()).when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThrows(RuntimeException.class, () -> authService.login(req));
    }

    @Test
    void login_success_returnsAuthResponse() {
        LoginRequest req = new LoginRequest();
        req.setEmail("a@b.com");
        req.setPassword("p");

        // authenticationManager does not throw
        User user = new User();
        user.setId(2L);
        user.setEmail("a@b.com");
        user.setFirstName("F");
        user.setLastName("L");

        when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken("a@b.com")).thenReturn("token-xyz");

        AuthResponse resp = authService.login(req);
        assertThat(resp.getToken()).isEqualTo("token-xyz");
    }
}
