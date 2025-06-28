package com.example.vehicleservice.repo;

import com.example.vehicleservice.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepo extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByUserId(Long userId);

    boolean existsByPlateNumber(String plateNumber);
}
