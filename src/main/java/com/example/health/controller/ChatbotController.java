package com.example.health.controller;

import com.example.health.service.AuthService;
import com.example.health.service.ChatbotService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/chatbot")
public class ChatbotController {
    
    @Autowired
    private ChatbotService chatbotService;
    
    @Autowired
    private AuthService authService;
    
    @GetMapping
    public String chatbotPage(HttpSession session, Model model) {
        if (!authService.isLoggedIn(session)) {
            return "redirect:/login";
        }
        
        model.addAttribute("chatHistory", session.getAttribute("chatHistory"));
        return "dashboard";
    }
    
    @PostMapping("/ask")
    @ResponseBody
    public String askQuestion(@RequestParam String question, HttpSession session) {
        if (!authService.isLoggedIn(session)) {
            return "Silakan login terlebih dahulu";
        }
        
        return chatbotService.getHealthAdvice(question);
    }
}