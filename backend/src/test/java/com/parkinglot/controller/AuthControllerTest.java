package com.parkinglot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkinglot.dto.AuthResponse;
import com.parkinglot.dto.LoginRequest;
import com.parkinglot.dto.RegisterRequest;
import com.parkinglot.dto.UserDTO;
import com.parkinglot.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private com.parkinglot.security.JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private com.parkinglot.security.JwtUtil jwtUtil;

    @MockBean
    private com.parkinglot.security.CustomUserDetailsService customUserDetailsService;

    @Test
    void register_returnsCreated() throws Exception {
        RegisterRequest req = new RegisterRequest("A","B","a@b.com","password123");

        AuthResponse resp = new AuthResponse("tok", new UserDTO(1L, "A","B","a@b.com"));
        when(authService.register(any(RegisterRequest.class))).thenReturn(resp);

        mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("tok"));
    }

    @Test
    void login_returnsOk() throws Exception {
        LoginRequest req = new LoginRequest("a@b.com","pass1234");
        AuthResponse resp = new AuthResponse("tok2", new UserDTO(2L, "C","D","a@b.com"));
        when(authService.login(any(LoginRequest.class))).thenReturn(resp);

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("tok2"));
    }

    @Test
    void getCurrentUser_returnsUser() throws Exception {
        UserDTO u = new UserDTO(3L, "X","Y","x@y.com");
        when(authService.getCurrentUser("x@y.com")).thenReturn(u);

        mvc.perform(get("/api/auth/me").principal(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken("x@y.com", null, java.util.Collections.emptyList())
            ))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("x@y.com"));
    }
}
