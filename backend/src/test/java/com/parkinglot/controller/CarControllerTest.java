package com.parkinglot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkinglot.dto.CarDTO;
import com.parkinglot.dto.CarRequest;
import com.parkinglot.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarController.class)
@AutoConfigureMockMvc(addFilters = false)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CarService carService;

    @MockBean
    private com.parkinglot.security.JwtUtil jwtUtil;

    @MockBean
    private com.parkinglot.security.CustomUserDetailsService customUserDetailsService;

    @Test
    void getUserCars_returnsList() throws Exception {
        CarDTO dto = new CarDTO(1L, "Make", "Model", "ABC1234", "Blue");
        when(carService.getUserCars("u@u.com")).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/cars").principal(new UsernamePasswordAuthenticationToken("u@u.com", "pw")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].licensePlate").value("ABC1234"));
    }

    @Test
    void addCar_success_returnsCreated() throws Exception {
        CarRequest req = new CarRequest("Make", "ABC1234", "Blue", "Model");
        CarDTO dto = new CarDTO(2L, "Make", "Model", "ABC1234", "Blue");
        when(carService.addCar(eq("u@u.com"), any(CarRequest.class))).thenReturn(dto);

        mockMvc.perform(post("/api/cars")
                        .principal(new UsernamePasswordAuthenticationToken("u@u.com", "pw"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.licensePlate").value("ABC1234"));
    }

    @Test
    void addCar_serviceThrows_returnsBadRequest() throws Exception {
        CarRequest req = new CarRequest("Make", "BADPLT1", "Red", "Model");
        when(carService.addCar(eq("u@u.com"), any(CarRequest.class))).thenThrow(new RuntimeException("INVALID_PLATE"));

        mockMvc.perform(post("/api/cars")
                        .principal(new UsernamePasswordAuthenticationToken("u@u.com", "pw"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("ERROR"))
                .andExpect(jsonPath("$.message").value("INVALID_PLATE"));
    }

    @Test
    void deleteCar_success_returnsNoContent() throws Exception {
        doNothing().when(carService).deleteCar("u@u.com", 5L);

        mockMvc.perform(delete("/api/cars/5").principal(new UsernamePasswordAuthenticationToken("u@u.com", "pw")))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCar_notFound_returnsNotFound() throws Exception {
        // service throws, controller should return ErrorResponse with 404
        org.mockito.Mockito.doThrow(new RuntimeException("NOT_FOUND")).when(carService).deleteCar("u@u.com", 6L);

        mockMvc.perform(delete("/api/cars/6").principal(new UsernamePasswordAuthenticationToken("u@u.com", "pw")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Car not found or not owned by user"));
    }
}
