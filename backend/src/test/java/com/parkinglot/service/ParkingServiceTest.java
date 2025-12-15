package com.parkinglot.service;

import com.parkinglot.dto.ParkingRequest;
import com.parkinglot.dto.ParkingSpotDTO;
import com.parkinglot.model.Car;
import com.parkinglot.model.ParkingSession;
import com.parkinglot.model.ParkingSpot;
import com.parkinglot.model.User;
import com.parkinglot.repository.CarRepository;
import com.parkinglot.repository.ParkingSessionRepository;
import com.parkinglot.repository.ParkingSpotRepository;
import com.parkinglot.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ParkingServiceTest {

    @Mock
    private ParkingSpotRepository parkingSpotRepository;

    @Mock
    private ParkingSessionRepository parkingSessionRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ParkingService parkingService;

    @Test
    void getAvailableSpots_byFloor() {
        ParkingSpot s = new ParkingSpot();
        s.setId(1L);
        s.setFloor(1);
        s.setSpotNumber(1);
        s.setOccupied(false);

        when(parkingSpotRepository.findByFloorAndOccupied(1, false)).thenReturn(List.of(s));

        var list = parkingService.getAvailableSpots(1);
        assertThat(list).hasSize(1);
        ParkingSpotDTO dto = list.get(0);
        assertThat(dto.getFloor()).isEqualTo(1);
    }

    @Test
    void parkCar_autoAssign_success() {
        User u = new User();
        u.setId(1L);
        u.setEmail("u@u.com");

        Car car = new Car();
        car.setId(2L);
        car.setUser(u);

        when(userRepository.findByEmail(u.getEmail())).thenReturn(Optional.of(u));
        when(carRepository.findByIdAndUser(2L, u)).thenReturn(Optional.of(car));
        when(parkingSessionRepository.existsByCarAndActive(car, true)).thenReturn(false);

        ParkingSpot spot = new ParkingSpot();
        spot.setId(10L);
        spot.setFloor(1);
        spot.setSpotNumber(5);
        spot.setOccupied(false);

        when(parkingSpotRepository.findByOccupied(false)).thenReturn(List.of(spot));

        ParkingSession savedSession = new ParkingSession();
        savedSession.setId(99L);
        savedSession.setCar(car);
        savedSession.setUser(u);
        savedSession.setParkingSpot(spot);

        when(parkingSessionRepository.save(any(ParkingSession.class))).thenReturn(savedSession);
        when(parkingSpotRepository.save(any(ParkingSpot.class))).thenReturn(spot);

        ParkingRequest req = new ParkingRequest();
        req.setCarId(2L);

        var dto = parkingService.parkCar(u.getEmail(), req);
        assertThat(dto).isNotNull();
        assertThat(dto.getCar().getId()).isEqualTo(2L);
    }

    @Test
    void parkCar_noSpots_throws() {
        User u = new User();
        u.setId(1L);
        u.setEmail("u@u.com");

        Car car = new Car();
        car.setId(2L);
        car.setUser(u);

        when(userRepository.findByEmail(u.getEmail())).thenReturn(Optional.of(u));
        when(carRepository.findByIdAndUser(2L, u)).thenReturn(Optional.of(car));
        when(parkingSessionRepository.existsByCarAndActive(car, true)).thenReturn(false);

        when(parkingSpotRepository.findByOccupied(false)).thenReturn(List.of());

        ParkingRequest req = new ParkingRequest();
        req.setCarId(2L);

        assertThrows(RuntimeException.class, () -> parkingService.parkCar(u.getEmail(), req));
    }

    @Test
    void leaveParkingSpot_success() {
        User user = new User(); user.setId(1L); user.setEmail("u@u.com");
        ParkingSpot spot = new ParkingSpot(); spot.setId(3L); spot.setOccupied(true);
        ParkingSession session = new ParkingSession(); session.setId(9L); session.setUser(user); session.setActive(true); session.setParkingSpot(spot);

        when(userRepository.findByEmail("u@u.com")).thenReturn(Optional.of(user));
        when(parkingSessionRepository.findByIdAndUser(9L, user)).thenReturn(Optional.of(session));

        parkingService.leaveParkingSpot("u@u.com", 9L);

        verify(parkingSessionRepository).save(any(ParkingSession.class));
        verify(parkingSpotRepository).save(any(ParkingSpot.class));
    }

    @Test
    void parkCar_carNotFound_throws() {
        User user = new User(); user.setEmail("a@b.com");
        when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.of(user));
        when(carRepository.findByIdAndUser(99L, user)).thenReturn(Optional.empty());

        ParkingRequest req = new ParkingRequest(); req.setCarId(99L);
        assertThrows(RuntimeException.class, () -> parkingService.parkCar("a@b.com", req));
    }
}
