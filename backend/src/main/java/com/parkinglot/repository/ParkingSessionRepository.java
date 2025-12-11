package com.parkinglot.repository;

import com.parkinglot.model.Car;
import com.parkinglot.model.ParkingSession;
import com.parkinglot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSessionRepository extends JpaRepository<ParkingSession, Long> {
    List<ParkingSession> findByUserAndActive(User user, Boolean active);
    Optional<ParkingSession> findByIdAndUser(Long id, User user);
    boolean existsByCarAndActive(Car car, Boolean active);
}
