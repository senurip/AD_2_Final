package com.example.parkingservice.repo;

import com.example.parkingservice.entity.ParkZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkZoneRepo extends JpaRepository<ParkZone, Long> {
}
