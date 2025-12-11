package com.parkinglot.controller;

import com.parkinglot.dto.ErrorResponse;
import com.parkinglot.dto.ParkingRequest;
import com.parkinglot.dto.ParkingSessionDTO;
import com.parkinglot.dto.ParkingSpotDTO;
import com.parkinglot.service.ParkingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ParkingController {
    
    private final ParkingService parkingService;
    
    @GetMapping("/spots/available")
    public ResponseEntity<List<ParkingSpotDTO>> getAvailableSpots(
            @RequestParam(required = false) Integer floor) {
        List<ParkingSpotDTO> spots = parkingService.getAvailableSpots(floor);
        return ResponseEntity.ok(spots);
    }
    
    @PostMapping("/parking")
    public ResponseEntity<?> parkCar(@Valid @RequestBody ParkingRequest request, Authentication authentication) {
        try {
            String email = authentication.getName();
            ParkingSessionDTO session = parkingService.parkCar(email, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(session);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(e.getMessage(), "Unable to park car"));
        }
    }
    
    @GetMapping("/parking/active")
    public ResponseEntity<List<ParkingSessionDTO>> getActiveSessions(Authentication authentication) {
        String email = authentication.getName();
        List<ParkingSessionDTO> sessions = parkingService.getActiveSessions(email);
        return ResponseEntity.ok(sessions);
    }
    
    @PostMapping("/parking/{sessionId}/leave")
    public ResponseEntity<?> leaveParkingSpot(@PathVariable Long sessionId, Authentication authentication) {
        try {
            String email = authentication.getName();
            parkingService.leaveParkingSpot(email, sessionId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage(), "Session not found"));
        }
    }
}
