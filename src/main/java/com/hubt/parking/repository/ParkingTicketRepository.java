package com.hubt.parking.repository;

import com.hubt.parking.model.ParkingTicket;
import com.hubt.parking.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface ParkingTicketRepository extends JpaRepository<ParkingTicket, Long> {
    Optional<ParkingTicket> findByVehicleAndIsParkedTrue(Vehicle vehicle);
    Optional<ParkingTicket> findByParkingCardCardNumberAndIsParkedTrue(String cardNumber);
    List<ParkingTicket> findByIsParkedTrue();
    List<ParkingTicket> findByIsParkedFalse();
}
