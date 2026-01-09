package com.example.health.controller;

import com.example.health.repository.PatientRepository;
import com.example.health.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
public class DashboardController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!authService.isLoggedIn(session)) {
            return "redirect:/login";
        }
        
        long totalPatients = patientRepository.count();
        long todayVisits = patientRepository.findAll().stream()
                .filter(p -> p.getLastVisit() != null && p.getLastVisit().equals(LocalDate.now()))
                .count();
        
        model.addAttribute("totalPatients", totalPatients);
        model.addAttribute("todayVisits", todayVisits);
        
        return "dashboard";
    }
}