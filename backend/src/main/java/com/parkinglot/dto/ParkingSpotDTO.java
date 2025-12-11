package com.parkinglot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSpotDTO {
    private Long id;
    private Integer floor;
    private Integer spotNumber;
    private Boolean occupied;
}
