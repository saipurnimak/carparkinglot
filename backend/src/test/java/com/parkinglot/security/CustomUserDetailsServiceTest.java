package com.parkinglot.security;

import com.parkinglot.model.User;
import com.parkinglot.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomUserDetailsServiceTest {

    @AfterEach
    void tearDown() {
        // no-op
    }

    @Test
    void loadUserByUsername_returnsUserDetails_whenUserExists() {
        UserRepository repo = mock(UserRepository.class);
        User user = new User();
        user.setEmail("alice@example.com");
        user.setPassword("secret");
        when(repo.findByEmail("alice@example.com")).thenReturn(Optional.of(user));

        CustomUserDetailsService svc = new CustomUserDetailsService(repo);
        UserDetails details = svc.loadUserByUsername("alice@example.com");

        assertNotNull(details);
        assertEquals("alice@example.com", details.getUsername());
        assertEquals("secret", details.getPassword());
    }

    @Test
    void loadUserByUsername_throws_whenNotFound() {
        UserRepository repo = mock(UserRepository.class);
        when(repo.findByEmail("bob@example.com")).thenReturn(Optional.empty());

        CustomUserDetailsService svc = new CustomUserDetailsService(repo);

        assertThrows(UsernameNotFoundException.class, () -> svc.loadUserByUsername("bob@example.com"));
    }
}
