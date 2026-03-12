package com.hubt.parking.service;

import com.hubt.parking.model.*;
import com.hubt.parking.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingService {
    private final StudentRepository studentRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingTicketRepository ticketRepository;
    private final ParkingCardRepository cardRepository;
    private final ParkingSlotRepository slotRepository;

    @Transactional
    public ParkingTicket parkVehicle(String licensePlate, String vehicleType, String cardNumber) {
        // 1. Verify card
        ParkingCard card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new RuntimeException("Thẻ không hợp lệ hoặc không tồn tại trong hệ thống."));
                
        if (!"ACTIVE".equals(card.getStatus())) {
            throw new RuntimeException("Thẻ đã bị khóa hoặc không hoạt động.");
        }
        
        // 2. Find or create vehicle
        Vehicle vehicle = vehicleRepository.findByLicensePlate(licensePlate)
            .orElseGet(() -> {
                Vehicle v = new Vehicle();
                v.setLicensePlate(licensePlate);
                v.setVehicleType(vehicleType);
                v.setOwner(card.getStudent()); // link if card has student
                return vehicleRepository.save(v);
            });

        // 3. Check if vehicle already parked
        if (ticketRepository.findByVehicleAndIsParkedTrue(vehicle).isPresent()) {
            throw new RuntimeException("Phương tiện " + licensePlate + " đang ở trong bãi rồi!");
        }
        
        // 4. Assign available slot
        ParkingSlot slot = slotRepository.findFirstByStatusOrderByIdAsc("AVAILABLE")
                .orElseThrow(() -> new RuntimeException("Hết chỗ đỗ xe trong bãi!"));

        ParkingTicket ticket = new ParkingTicket();
        ticket.setVehicle(vehicle);
        ticket.setParkingCard(card);
        ticket.setParkingSlot(slot);
        ticket.setEntryTime(LocalDateTime.now());
        ticket.setParked(true);
        
        // Update slot status
        slot.setStatus("OCCUPIED");
        slotRepository.save(slot);

        return ticketRepository.save(ticket);
    }

    @Transactional
    public ParkingTicket takeVehicleOut(String identifier) {
        // identifier Can be card number or plate
        ParkingTicket ticket = ticketRepository.findByParkingCardCardNumberAndIsParkedTrue(identifier).orElse(null);
        
        if (ticket == null) {
            Vehicle vehicle = vehicleRepository.findByLicensePlate(identifier)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phương tiện hoặc thẻ đang gửi trong bãi."));

            ticket = ticketRepository.findByVehicleAndIsParkedTrue(vehicle)
                .orElseThrow(() -> new RuntimeException("Phương tiện này hiện không ở trong bãi."));
        }

        ticket.setExitTime(LocalDateTime.now());
        ticket.setParked(false);
        boolean isStudent = ticket.getParkingCard() != null && ticket.getParkingCard().getStudent() != null;
        ticket.setFee(calculateFee(ticket.getEntryTime(), ticket.getExitTime(), ticket.getVehicle().getVehicleType(), isStudent));
        
        // Free the slot
        ParkingSlot slot = ticket.getParkingSlot();
        if (slot != null) {
            slot.setStatus("AVAILABLE");
            slotRepository.save(slot);
        }
        
        return ticketRepository.save(ticket);
    }

    private Double calculateFee(LocalDateTime entry, LocalDateTime exit, String type, boolean isStudent) {
        if (isStudent) {
            return 0.0; // Miễn phí cho sinh viên có thẻ gắn với tài khoản
        }
        long hours = Duration.between(entry, exit).toHours();
        double baseFee = "Xe Đạp".equalsIgnoreCase(type) ? 2000.0 : 5000.0;
        return baseFee + (hours > 24 ? (hours / 24) * baseFee : 0);
    }

    public List<ParkingTicket> getCurrentlyParked() {
        return ticketRepository.findByIsParkedTrue();
    }
}
