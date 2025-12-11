package com.parkinglot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSessionDTO {
    private Long parkingSessionId;
    private CarDTO car;
    private ParkingSpotDTO spot;
    private LocalDateTime startTime;
}
