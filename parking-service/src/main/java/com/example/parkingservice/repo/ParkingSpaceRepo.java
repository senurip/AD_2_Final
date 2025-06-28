package com.example.parkingservice.repo;

import com.example.parkingservice.entity.ParkingSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingSpaceRepo extends JpaRepository<ParkingSpace, Long> {
}
