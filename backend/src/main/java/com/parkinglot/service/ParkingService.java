package com.parkinglot.service;

import com.parkinglot.dto.*;
import com.parkinglot.model.*;
import com.parkinglot.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParkingService {
    
    private final ParkingSpotRepository parkingSpotRepository;
    private final ParkingSessionRepository parkingSessionRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    
    public List<ParkingSpotDTO> getAvailableSpots(Integer floor) {
        List<ParkingSpot> spots;
        if (floor != null) {
            spots = parkingSpotRepository.findByFloorAndOccupied(floor, false);
        } else {
            spots = parkingSpotRepository.findByOccupied(false);
        }
        
        return spots.stream()
                .map(this::convertSpotToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public ParkingSessionDTO parkCar(String email, ParkingRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Verify car belongs to user
        Car car = carRepository.findByIdAndUser(request.getCarId(), user)
                .orElseThrow(() -> new RuntimeException("Car not found"));
        
        // Find parking spot
        ParkingSpot spot = parkingSpotRepository.findByFloorAndSpotNumber(request.getFloor(), request.getSpotNumber())
                .orElseThrow(() -> new RuntimeException("Parking spot not found"));
        
        // Check if spot is already occupied
        if (spot.getOccupied()) {
            throw new RuntimeException("SPOT_ALREADY_OCCUPIED");
        }
        
        // Create parking session
        ParkingSession session = new ParkingSession();
        session.setUser(user);
        session.setCar(car);
        session.setParkingSpot(spot);
        session.setStartTime(LocalDateTime.now());
        session.setActive(true);
        
        session = parkingSessionRepository.save(session);
        
        // Mark spot as occupied
        spot.setOccupied(true);
        spot.setCurrentParkingSession(session);
        parkingSpotRepository.save(spot);
        
        return convertSessionToDTO(session);
    }
    
    public List<ParkingSessionDTO> getActiveSessions(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return parkingSessionRepository.findByUserAndActive(user, true).stream()
                .map(this::convertSessionToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void leaveParkingSpot(String email, Long sessionId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        ParkingSession session = parkingSessionRepository.findByIdAndUser(sessionId, user)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        
        // Mark session as inactive
        session.setActive(false);
        session.setEndTime(LocalDateTime.now());
        parkingSessionRepository.save(session);
        
        // Mark spot as available
        ParkingSpot spot = session.getParkingSpot();
        spot.setOccupied(false);
        spot.setCurrentParkingSession(null);
        parkingSpotRepository.save(spot);
    }
    
    private ParkingSpotDTO convertSpotToDTO(ParkingSpot spot) {
        return new ParkingSpotDTO(
                spot.getId(),
                spot.getFloor(),
                spot.getSpotNumber(),
                spot.getOccupied()
        );
    }
    
    private ParkingSessionDTO convertSessionToDTO(ParkingSession session) {
        CarDTO carDTO = new CarDTO(
                session.getCar().getId(),
                session.getCar().getMake(),
                session.getCar().getModel(),
                session.getCar().getLicensePlate(),
                session.getCar().getColor()
        );
        
        ParkingSpotDTO spotDTO = convertSpotToDTO(session.getParkingSpot());
        
        return new ParkingSessionDTO(
                session.getId(),
                carDTO,
                spotDTO,
                session.getStartTime()
        );
    }
}
