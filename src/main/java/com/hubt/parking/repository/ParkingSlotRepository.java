package com.hubt.parking.repository;

import com.hubt.parking.model.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {
    Optional<ParkingSlot> findFirstByStatusOrderByIdAsc(String status);
}
