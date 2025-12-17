package com.parkinglot.repository;

import com.parkinglot.model.ParkingSpot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ParkingSpotRepositoryTest {

    @Autowired
    private ParkingSpotRepository repository;

    @Test
    void findByOccupiedAndFloor() {
        ParkingSpot s1 = new ParkingSpot();
        s1.setFloor(1);
        s1.setSpotNumber(1);
        s1.setOccupied(false);
        repository.save(s1);

        ParkingSpot s2 = new ParkingSpot();
        s2.setFloor(2);
        s2.setSpotNumber(1);
        s2.setOccupied(true);
        repository.save(s2);

        List<ParkingSpot> free = repository.findByOccupied(false);
        assertThat(free).hasSize(1);

        List<ParkingSpot> floor1 = repository.findByFloorAndOccupied(1, false);
        assertThat(floor1).hasSize(1);

        assertThat(repository.findByFloorAndSpotNumber(1, 1)).isPresent();
    }
}
