package com.parkinglot.repository;

import com.parkinglot.model.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
    List<ParkingSpot> findByOccupied(Boolean occupied);
    List<ParkingSpot> findByFloorAndOccupied(Integer floor, Boolean occupied);
    Optional<ParkingSpot> findByFloorAndSpotNumber(Integer floor, Integer spotNumber);
}
