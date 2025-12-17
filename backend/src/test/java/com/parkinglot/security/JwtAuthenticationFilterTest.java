package com.parkinglot.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtAuthenticationFilterTest {

    @AfterEach
    void cleanup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilter_setsAuthentication_whenTokenValid() throws ServletException, IOException {
        String token = "valid.jwt.token";
        String email = "test@example.com";

        JwtUtil jwtUtil = mock(JwtUtil.class);
        CustomUserDetailsService userDetailsService = mock(CustomUserDetailsService.class);

        when(jwtUtil.extractEmail(token)).thenReturn(email);
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(email, "pw", java.util.Collections.emptyList());
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtUtil.validateToken(token, userDetails)).thenReturn(true);

        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil, userDetailsService);

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse resp = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(req, resp, chain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(email, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Test
    void doFilter_doesNotSetAuthentication_whenTokenInvalid() throws ServletException, IOException {
        String token = "bad.jwt";

        JwtUtil jwtUtil = mock(JwtUtil.class);
        CustomUserDetailsService userDetailsService = mock(CustomUserDetailsService.class);

        when(jwtUtil.extractEmail(token)).thenThrow(new RuntimeException("invalid"));

        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil, userDetailsService);

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse resp = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(req, resp, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
