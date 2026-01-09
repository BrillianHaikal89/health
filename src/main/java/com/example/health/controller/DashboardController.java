package com.example.health.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.health.model.Patient;
import com.example.health.repository.PatientRepository;
import com.example.health.service.AuthService;

import jakarta.servlet.http.HttpSession;

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

    @GetMapping("/api/patients")
    @ResponseBody
    public List<Patient> getAllPatients(HttpSession session) {
        if (!authService.isLoggedIn(session)) {
            return List.of();
        }
        return patientRepository.findAll();
    }

    @GetMapping("/api/patients/search")
    @ResponseBody
    public List<Patient> searchPatients(@RequestParam String name, HttpSession session) {
        if (!authService.isLoggedIn(session)) {
            return List.of();
        }
        return patientRepository.findByNameContainingIgnoreCase(name);
    }

    @GetMapping("/api/patients/{id}")
    @ResponseBody
    public ResponseEntity<?> getPatient(@PathVariable String id, HttpSession session) {
        if (!authService.isLoggedIn(session)) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Unauthorized"));
        }

        return patientRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api/patients")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addPatient(@RequestBody Patient patient, HttpSession session) {
        if (!authService.isLoggedIn(session)) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Unauthorized"));
        }

        try {
            patient.setLastVisit(LocalDate.now());
            Patient savedPatient = patientRepository.save(patient);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Patient added successfully");
            response.put("patient", savedPatient);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to add patient: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/api/patients/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updatePatient(@PathVariable String id,
            @RequestBody Patient patient,
            HttpSession session) {
        if (!authService.isLoggedIn(session)) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Unauthorized"));
        }

        try {
            if (!patientRepository.existsById(id)) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Patient not found");
                return ResponseEntity.notFound().build();
            }

            patient.setId(id);
            if (patient.getLastVisit() == null) {
                patient.setLastVisit(LocalDate.now());
            }

            Patient updatedPatient = patientRepository.save(patient);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Patient updated successfully");
            response.put("patient", updatedPatient);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to update patient: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/api/patients/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deletePatient(@PathVariable String id, HttpSession session) {
        if (!authService.isLoggedIn(session)) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Unauthorized"));
        }

        try {
            if (!patientRepository.existsById(id)) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Patient not found");
                return ResponseEntity.notFound().build();
            }

            patientRepository.deleteById(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Patient deleted successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to delete patient: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
