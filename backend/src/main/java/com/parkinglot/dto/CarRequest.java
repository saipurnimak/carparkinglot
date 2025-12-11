package com.parkinglot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarRequest {
    
    @NotBlank(message = "Make is required")
    private String make;
    
    @NotBlank(message = "License plate is required")
    @Pattern(regexp = "^[A-Z0-9]{7}$", message = "License plate must be 7 letters/numbers")
    private String licensePlate;
    
    private String color;
    
    @NotBlank(message = "Model is required")
    private String model;
}
