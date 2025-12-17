package com.parkinglot.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    @Test
    void generateAndValidateToken() throws Exception {
        JwtUtil jwtUtil = new JwtUtil();

        // Inject required private fields since JwtUtil normally relies on Spring @Value injection
        java.lang.reflect.Field secretField = JwtUtil.class.getDeclaredField("secret");
        secretField.setAccessible(true);
        // 32+ byte key for HS256
        secretField.set(jwtUtil, "01234567890123456789012345678901");

        java.lang.reflect.Field expField = JwtUtil.class.getDeclaredField("expiration");
        expField.setAccessible(true);
        expField.set(jwtUtil, 86400000L);

        String email = "test@example.com";
        String token = jwtUtil.generateToken(email);

        assertNotNull(token);
        String extracted = jwtUtil.extractEmail(token);
        assertEquals(email, extracted);

        User userDetails = new User(email, "", new ArrayList<>());
        assertTrue(jwtUtil.validateToken(token, userDetails));
    }
}
