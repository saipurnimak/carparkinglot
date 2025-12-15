package com.parkinglot.repository;

import com.parkinglot.model.Car;
import com.parkinglot.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveAndFindByUser() {
        User u = new User();
        u.setFirstName("T");
        u.setLastName("U");
        u.setEmail("test@repo.com");
        u.setPassword("testpass");
        userRepository.save(u);

        Car c = new Car();
        c.setMake("Make");
        c.setModel("Model");
        c.setLicensePlate("ZZZ1111");
        c.setUser(u);
        carRepository.save(c);

        List<Car> list = carRepository.findByUser(u);
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getLicensePlate()).isEqualTo("ZZZ1111");
    }
}
