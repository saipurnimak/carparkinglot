package com.parkinglot.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class SecurityConfigTest {

    @Test
    void passwordEncoder_isBCrypt_and_providerIsDao() throws Exception {
        // mocks for constructor
        com.parkinglot.security.JwtAuthenticationFilter jwtFilter = mock(com.parkinglot.security.JwtAuthenticationFilter.class);
        UserDetailsService userDetailsService = mock(UserDetailsService.class);

        SecurityConfig cfg = new SecurityConfig(jwtFilter, userDetailsService);

        assertNotNull(cfg.passwordEncoder());
        assertTrue(cfg.passwordEncoder() instanceof org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder);

        AuthenticationProvider provider = cfg.authenticationProvider();
        assertNotNull(provider);
        assertTrue(provider instanceof org.springframework.security.authentication.dao.DaoAuthenticationProvider);
    }

    @Test
    void corsConfiguration_hasAllowedOrigins() {
        // mocks for constructor
        com.parkinglot.security.JwtAuthenticationFilter jwtFilter = mock(com.parkinglot.security.JwtAuthenticationFilter.class);
        UserDetailsService userDetailsService = mock(UserDetailsService.class);

        SecurityConfig cfg = new SecurityConfig(jwtFilter, userDetailsService);
        CorsConfigurationSource source = cfg.corsConfigurationSource();

        assertTrue(source instanceof UrlBasedCorsConfigurationSource);
        UrlBasedCorsConfigurationSource urlSource = (UrlBasedCorsConfigurationSource) source;

        CorsConfiguration cfgItem = urlSource.getCorsConfiguration(new MockHttpServletRequest("GET", "/"));
        assertNotNull(cfgItem);
        assertTrue(cfgItem.getAllowedOrigins().contains("http://localhost:3000"));
    }
}
