package com.parkinglot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkinglot.dto.ParkingRequest;
import com.parkinglot.dto.ParkingSessionDTO;
import com.parkinglot.dto.ParkingSpotDTO;
import com.parkinglot.dto.CarDTO;
import com.parkinglot.service.ParkingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ParkingController.class)
@AutoConfigureMockMvc(addFilters = false)
class ParkingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ParkingService parkingService;

    @MockBean
    private com.parkinglot.security.JwtUtil jwtUtil;

    @MockBean
    private com.parkinglot.security.CustomUserDetailsService customUserDetailsService;

    @Test
    void getAvailableSpots_returnsList() throws Exception {
        ParkingSpotDTO spot = new ParkingSpotDTO(10L, 1, 5, false);
        when(parkingService.getAvailableSpots(null)).thenReturn(List.of(spot));

        mockMvc.perform(get("/api/spots/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].spotNumber").value(5));
    }

    @Test
    void parkCar_success_returnsCreated() throws Exception {
        ParkingRequest req = new ParkingRequest(2L, null, null);
        ParkingSpotDTO spot = new ParkingSpotDTO(11L, 1, 7, true);
        CarDTO car = new CarDTO(2L, "Make", "Model", "ABC123", "Blue");
        ParkingSessionDTO session = new ParkingSessionDTO(100L, car, spot, LocalDateTime.now());

        when(parkingService.parkCar(eq("u@u.com"), any(ParkingRequest.class))).thenReturn(session);

        mockMvc.perform(post("/api/parking/park")
                        .principal(new UsernamePasswordAuthenticationToken("u@u.com", "pw"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.parkingSessionId").value(100));
    }

    @Test
    void parkCar_noSpots_returnsServiceUnavailable() throws Exception {
        ParkingRequest req = new ParkingRequest(3L, null, null);
        when(parkingService.parkCar(eq("u@u.com"), any(ParkingRequest.class)))
                .thenThrow(new RuntimeException("NO_SPOTS_AVAILABLE"));

        mockMvc.perform(post("/api/parking/park")
                        .principal(new UsernamePasswordAuthenticationToken("u@u.com", "pw"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.error").value("NO_SPOTS_AVAILABLE"));
    }

    @Test
    void getActiveSessions_returnsList() throws Exception {
        ParkingSpotDTO spot = new ParkingSpotDTO(12L, 2, 3, true);
        CarDTO car = new CarDTO(4L, "Make", "Model", "XYZ999", "Red");
        ParkingSessionDTO session = new ParkingSessionDTO(200L, car, spot, LocalDateTime.now());
        when(parkingService.getActiveSessions("u@u.com")).thenReturn(List.of(session));

        mockMvc.perform(get("/api/parking/active").principal(new UsernamePasswordAuthenticationToken("u@u.com", "pw")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].parkingSessionId").value(200));
    }

    @Test
    void leaveParking_success_returnsOk() throws Exception {
        doNothing().when(parkingService).leaveParkingSpot("u@u.com", 5L);

        mockMvc.perform(post("/api/parking/5/leave").principal(new UsernamePasswordAuthenticationToken("u@u.com", "pw")))
                .andExpect(status().isOk());
    }

    @Test
    void leaveParking_notFound_returnsNotFound() throws Exception {
        doThrow(new RuntimeException("NOT_FOUND")).when(parkingService).leaveParkingSpot("u@u.com", 6L);

        mockMvc.perform(post("/api/parking/6/leave").principal(new UsernamePasswordAuthenticationToken("u@u.com", "pw")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"));
    }
}
