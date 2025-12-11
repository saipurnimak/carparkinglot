package com.parkinglot.controller;

import com.parkinglot.dto.CarDTO;
import com.parkinglot.dto.CarRequest;
import com.parkinglot.dto.ErrorResponse;
import com.parkinglot.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {
    
    private final CarService carService;
    
    @GetMapping
    public ResponseEntity<List<CarDTO>> getUserCars(Authentication authentication) {
        String email = authentication.getName();
        List<CarDTO> cars = carService.getUserCars(email);
        return ResponseEntity.ok(cars);
    }
    
    @PostMapping
    public ResponseEntity<?> addCar(@Valid @RequestBody CarRequest request, Authentication authentication) {
        try {
            String email = authentication.getName();
            CarDTO car = carService.addCar(email, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(car);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("ERROR", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{carId}")
    public ResponseEntity<?> deleteCar(@PathVariable Long carId, Authentication authentication) {
        try {
            String email = authentication.getName();
            carService.deleteCar(email, carId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage(), "Car not found or not owned by user"));
        }
    }
}
