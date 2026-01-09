package com.example.health.service;

import com.example.health.model.User;
import com.example.health.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    public boolean login(String username, String password, HttpSession session) {
        return userRepository.findByUsernameAndPassword(username, password)
                .map(user -> {
                    session.setAttribute("user", user);
                    session.setAttribute("username", user.getUsername());
                    session.setAttribute("role", user.getRole());
                    return true;
                })
                .orElse(false);
    }
    
    public void logout(HttpSession session) {
        session.invalidate();
    }
    
    public boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("user") != null;
    }
}