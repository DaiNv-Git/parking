package com.hubt.parking.controller;

import com.hubt.parking.model.ParkingCard;
import com.hubt.parking.repository.ParkingCardRepository;
import com.hubt.parking.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class CardController {

    private final ParkingCardRepository cardRepository;
    private final StudentRepository studentRepository;

    @GetMapping("/cards")
    public String listCards(Model model) {
        model.addAttribute("cards", cardRepository.findAll());
        model.addAttribute("students", studentRepository.findAll());
        return "cards";
    }

    @PostMapping("/cards/add")
    public String addCard(@RequestParam String cardNumber, @RequestParam(required = false) Long studentId, RedirectAttributes redirectAttributes) {
        try {
            ParkingCard card = new ParkingCard();
            card.setCardNumber(cardNumber);
            card.setStatus("ACTIVE");
            if (studentId != null) {
                card.setStudent(studentRepository.findById(studentId).orElse(null));
            }
            cardRepository.save(card);
            redirectAttributes.addFlashAttribute("success", "Tạo thẻ thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/cards";
    }

    @PostMapping("/cards/toggle/{id}")
    public String toggleCardStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            var card = cardRepository.findById(id).orElseThrow();
            card.setStatus("ACTIVE".equals(card.getStatus()) ? "LOCKED" : "ACTIVE");
            cardRepository.save(card);
            redirectAttributes.addFlashAttribute("success", "Cập nhật trạng thái thẻ thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/cards";
    }
}
