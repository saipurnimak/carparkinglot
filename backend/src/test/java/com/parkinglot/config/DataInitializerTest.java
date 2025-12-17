package com.parkinglot.config;

import com.parkinglot.model.ParkingSpot;
import com.parkinglot.repository.ParkingSpotRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DataInitializerTest {

    @Test
    void run_whenNoSpots_initializes75Spots() throws Exception {
        ParkingSpotRepository repo = Mockito.mock(ParkingSpotRepository.class);
        when(repo.count()).thenReturn(0L);

        DataInitializer init = new DataInitializer(repo);
        init.run();

        verify(repo, times(75)).save(any(ParkingSpot.class));
    }

    @Test
    void run_whenSpotsExist_doesNotInitialize() throws Exception {
        ParkingSpotRepository repo = Mockito.mock(ParkingSpotRepository.class);
        when(repo.count()).thenReturn(10L);

        DataInitializer init = new DataInitializer(repo);
        init.run();

        verify(repo, times(0)).save(any(ParkingSpot.class));
    }
}
