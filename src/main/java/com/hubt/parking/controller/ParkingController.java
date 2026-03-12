package com.hubt.parking.controller;

import com.hubt.parking.repository.ParkingCardRepository;
import com.hubt.parking.service.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ParkingController {

    private final ParkingService parkingService;
    private final ParkingCardRepository parkingCardRepository;

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("parkedVehicles", parkingService.getCurrentlyParked());
        model.addAttribute("activeCards", parkingCardRepository.findByStatus("ACTIVE"));
        return "index";
    }

    @PostMapping("/park")
    public String parkVehicle(@RequestParam String licensePlate,
                              @RequestParam String vehicleType,
                              @RequestParam String cardNumber,
                              RedirectAttributes redirectAttributes) {
        try {
            var ticket = parkingService.parkVehicle(licensePlate, vehicleType, cardNumber);
            redirectAttributes.addFlashAttribute("success", "Vào bãi thành công. Biển số: " + licensePlate + " | Vị trí: " + ticket.getParkingSlot().getLocation());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/take-out")
    public String takeVehicleOut(@RequestParam String identifier, RedirectAttributes redirectAttributes) {
        try {
            var ticket = parkingService.takeVehicleOut(identifier);
            redirectAttributes.addFlashAttribute("success", "Xe ra khỏi bãi thành công. Số tiền thu: " + ticket.getFee() + " VNĐ");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/";
    }
}
