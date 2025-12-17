package com.parkinglot.repository;

import com.parkinglot.model.Car;
import com.parkinglot.model.ParkingSession;
import com.parkinglot.model.ParkingSpot;
import com.parkinglot.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ParkingSessionRepositoryTest {

    @Autowired
    private ParkingSessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ParkingSpotRepository parkingSpotRepository;

    @Test
    void findByUserAndActive_andExistsByCarAndActive_andFindByIdAndUser() {
        User u = new User();
        u.setFirstName("P");
        u.setLastName("S");
        u.setEmail("ps@example.com");
        u.setPassword("pw");
        userRepository.save(u);

        Car car = new Car();
        car.setMake("M");
        car.setModel("Mo");
        car.setLicensePlate("PL123");
        car.setUser(u);
        carRepository.save(car);

        ParkingSpot spot = new ParkingSpot();
        spot.setFloor(1);
        spot.setSpotNumber(1);
        spot.setOccupied(true);
        parkingSpotRepository.save(spot);

        ParkingSession session = new ParkingSession();
        session.setUser(u);
        session.setCar(car);
        session.setParkingSpot(spot);
        session.setStartTime(LocalDateTime.now());
        session.setActive(true);
        sessionRepository.save(session);

        List<ParkingSession> active = sessionRepository.findByUserAndActive(u, true);
        assertThat(active).hasSize(1);

        assertThat(sessionRepository.existsByCarAndActive(car, true)).isTrue();

        assertThat(sessionRepository.findByIdAndUser(session.getId(), u)).isPresent();
    }
}
