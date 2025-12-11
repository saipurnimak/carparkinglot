package com.parkinglot.service;

import com.parkinglot.dto.CarDTO;
import com.parkinglot.dto.CarRequest;
import com.parkinglot.model.Car;
import com.parkinglot.model.User;
import com.parkinglot.repository.CarRepository;
import com.parkinglot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarService {
    
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    
    public List<CarDTO> getUserCars(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return carRepository.findByUser(user).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public CarDTO addCar(String email, CarRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Car car = new Car();
        car.setMake(request.getMake());
        car.setModel(request.getModel());
        car.setLicensePlate(request.getLicensePlate().toUpperCase());
        car.setColor(request.getColor());
        car.setUser(user);
        
        car = carRepository.save(car);
        return convertToDTO(car);
    }
    
    public void deleteCar(String email, Long carId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Car car = carRepository.findByIdAndUser(carId, user)
                .orElseThrow(() -> new RuntimeException("CAR_NOT_FOUND"));
        
        carRepository.delete(car);
    }
    
    private CarDTO convertToDTO(Car car) {
        return new CarDTO(
                car.getId(),
                car.getMake(),
                car.getModel(),
                car.getLicensePlate(),
                car.getColor()
        );
    }
}
