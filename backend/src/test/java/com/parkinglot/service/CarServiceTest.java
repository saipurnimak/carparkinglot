package com.parkinglot.service;

import com.parkinglot.dto.CarDTO;
import com.parkinglot.dto.CarRequest;
import com.parkinglot.model.Car;
import com.parkinglot.model.User;
import com.parkinglot.repository.CarRepository;
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

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CarService carService;

    @Test
    void getUserCars_returnsList() {
        User u = new User();
        u.setId(1L);
        u.setEmail("a@b.com");

        Car c = new Car();
        c.setId(10L);
        c.setMake("Toyota");
        c.setModel("Corolla");
        c.setLicensePlate("ABC1234");
        c.setUser(u);

        when(userRepository.findByEmail(u.getEmail())).thenReturn(Optional.of(u));
        when(carRepository.findByUser(u)).thenReturn(List.of(c));

        List<CarDTO> list = carService.getUserCars(u.getEmail());
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getLicensePlate()).isEqualTo("ABC1234");
    }

    @Test
    void addCar_success() {
        User u = new User();
        u.setId(2L);
        u.setEmail("x@y.com");

        CarRequest req = new CarRequest("Honda","XYZ9876","Red","Civic");

        when(userRepository.findByEmail(u.getEmail())).thenReturn(Optional.of(u));

        Car saved = new Car();
        saved.setId(5L);
        saved.setMake(req.getMake());
        saved.setModel(req.getModel());
        saved.setLicensePlate(req.getLicensePlate());
        saved.setColor(req.getColor());
        saved.setUser(u);

        when(carRepository.save(any(Car.class))).thenReturn(saved);

        CarDTO dto = carService.addCar(u.getEmail(), req);
        assertThat(dto.getId()).isEqualTo(5L);
        assertThat(dto.getLicensePlate()).isEqualTo(req.getLicensePlate());
    }

    @Test
    void deleteCar_notFoundThrows() {
        User u = new User();
        u.setId(3L);
        u.setEmail("z@z.com");

        when(userRepository.findByEmail(u.getEmail())).thenReturn(Optional.of(u));
        when(carRepository.findByIdAndUser(99L, u)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> carService.deleteCar(u.getEmail(), 99L));
    }
}
