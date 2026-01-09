package com.example.health.controller;

import com.example.health.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @GetMapping("/")
    public String home(HttpSession session) {
        if (authService.isLoggedIn(session)) {
            return "redirect:/dashboard";
        }
        return "login";
    }
    
    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (authService.isLoggedIn(session)) {
            return "redirect:/dashboard";
        }
        return "login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String username, 
                       @RequestParam String password,
                       HttpSession session,
                       Model model) {
        if (authService.login(username, password, session)) {
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Username atau password salah!");
            return "login";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        authService.logout(session);
        return "redirect:/login";
    }
}