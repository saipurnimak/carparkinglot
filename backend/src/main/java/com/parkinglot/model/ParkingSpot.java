package com.parkinglot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parking_spots", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"floor", "spotNumber"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSpot {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Integer floor;
    
    @Column(nullable = false)
    private Integer spotNumber;
    
    @Column(nullable = false)
    private Boolean occupied = false;
    
    @OneToOne(mappedBy = "parkingSpot")
    private ParkingSession currentParkingSession;
}
