package com.hubt.parking.controller;

import com.hubt.parking.model.ParkingTicket;
import com.hubt.parking.repository.ParkingTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReportController {

    private final ParkingTicketRepository ticketRepository;

    @GetMapping("/reports")
    public String reports(Model model) {
        List<ParkingTicket> allTickets = ticketRepository.findAll();
        
        long totalRevenue = allTickets.stream()
                .filter(t -> t.getFee() != null)
                .mapToLong(t -> t.getFee().longValue())
                .sum();
                
        long currentlyParked = allTickets.stream()
                .filter(ParkingTicket::isParked)
                .count();

        model.addAttribute("tickets", allTickets);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("currentlyParked", currentlyParked);
        model.addAttribute("totalTickets", allTickets.size());
        
        return "reports";
    }
}
