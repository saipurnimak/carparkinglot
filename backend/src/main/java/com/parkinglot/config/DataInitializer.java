package com.parkinglot.config;

import com.parkinglot.model.ParkingSpot;
import com.parkinglot.repository.ParkingSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final ParkingSpotRepository parkingSpotRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Check if parking spots already exist
        if (parkingSpotRepository.count() == 0) {
            // Create parking spots for 3 floors with varying number of spots per floor
            initializeParkingSpots();
        }
    }
    
    private void initializeParkingSpots() {
        // Floor 1: spots 1-20
        for (int i = 1; i <= 20; i++) {
            createParkingSpot(1, i);
        }
        
        // Floor 2: spots 1-25
        for (int i = 1; i <= 25; i++) {
            createParkingSpot(2, i);
        }
        
        // Floor 3: spots 1-30
        for (int i = 1; i <= 30; i++) {
            createParkingSpot(3, i);
        }
        
        System.out.println("Initialized parking garage with 75 spots across 3 floors");
    }
    
    private void createParkingSpot(int floor, int spotNumber) {
        ParkingSpot spot = new ParkingSpot();
        spot.setFloor(floor);
        spot.setSpotNumber(spotNumber);
        spot.setOccupied(false);
        parkingSpotRepository.save(spot);
    }
}
