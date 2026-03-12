package com.hubt.parking.repository;

import com.hubt.parking.model.ParkingCard;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ParkingCardRepository extends JpaRepository<ParkingCard, Long> {
    Optional<ParkingCard> findByCardNumber(String cardNumber);
    List<ParkingCard> findByStatus(String status);
}
