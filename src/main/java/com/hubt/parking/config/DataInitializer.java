package com.hubt.parking.config;

import com.hubt.parking.model.*;
import com.hubt.parking.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ParkingSlotRepository slotRepository;
    private final ParkingCardRepository cardRepository;
    private final StudentRepository studentRepository;

    @Override
    public void run(String... args) {
        // Create default parking slots if empty
        if (slotRepository.count() == 0) {
            for (int i = 1; i <= 20; i++) {
                slotRepository.save(new ParkingSlot(null, "Khung A - Vị trí " + i, "AVAILABLE"));
            }
        }
        
        // Create sample students and cards
        if (studentRepository.count() == 0) {
            Student s1 = new Student(null, "SV001", "Nguyễn Văn A", "CT1", "0901234567");
            studentRepository.save(s1);
            ParkingCard c1 = new ParkingCard(null, "CARD001", "ACTIVE", s1);
            cardRepository.save(c1);
            
            Student s2 = new Student(null, "SV002", "Trần Thị B", "CT2", "0987654321");
            studentRepository.save(s2);
            ParkingCard c2 = new ParkingCard(null, "CARD002", "ACTIVE", s2);
            cardRepository.save(c2);
            
            // Guest Card
            ParkingCard guestCard = new ParkingCard(null, "CARD_GUEST", "ACTIVE", null);
            cardRepository.save(guestCard);
        }
    }
}
