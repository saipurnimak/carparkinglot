package com.parkinglot.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingRequest {
    
    @NotNull(message = "Car ID is required")
    private Long carId;
    
    @NotNull(message = "Floor is required")
    private Integer floor;
    
    @NotNull(message = "Spot number is required")
    private Integer spotNumber;
}
