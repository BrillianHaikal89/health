package com.example.health.controller;

import com.example.health.model.Patient;
import com.example.health.repository.PatientRepository;
import com.example.health.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/patients")
public class PatientController {
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private AuthService authService;
    
    @GetMapping
    public String listPatients(HttpSession session, Model model) {
        if (!authService.isLoggedIn(session)) {
            return "redirect:/login";
        }
        
        List<Patient> patients = patientRepository.findAll();
        model.addAttribute("patients", patients);
        model.addAttribute("newPatient", new Patient());
        return "patients";
    }
    
    @PostMapping("/add")
    public String addPatient(@ModelAttribute Patient patient, HttpSession session) {
        if (!authService.isLoggedIn(session)) {
            return "redirect:/login";
        }
        
        patient.setLastVisit(LocalDate.now());
        patientRepository.save(patient);
        return "redirect:/patients";
    }
    
    @GetMapping("/edit/{id}")
    @ResponseBody
    public Patient getPatient(@PathVariable String id, HttpSession session) {
        if (!authService.isLoggedIn(session)) {
            return null;
        }
        
        return patientRepository.findById(id).orElse(null);
    }
    
    @PostMapping("/update/{id}")
    public String updatePatient(@PathVariable String id, 
                               @ModelAttribute Patient patient,
                               HttpSession session) {
        if (!authService.isLoggedIn(session)) {
            return "redirect:/login";
        }
        
        patient.setId(id);
        patientRepository.save(patient);
        return "redirect:/patients";
    }
    
    @GetMapping("/delete/{id}")
    public String deletePatient(@PathVariable String id, HttpSession session) {
        if (!authService.isLoggedIn(session)) {
            return "redirect:/login";
        }
        
        patientRepository.deleteById(id);
        return "redirect:/patients";
    }
}