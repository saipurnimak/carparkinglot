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
    
    // Optional: if not provided, system will auto-assign available spot
    private Integer floor;
    
    // Optional: if not provided, system will auto-assign available spot
    private Integer spotNumber;
}
