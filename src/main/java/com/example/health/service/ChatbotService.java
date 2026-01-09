package com.example.health.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatbotService {
    
    @Value("${mistral.api.url}")
    private String apiUrl;
    
    @Value("${mistral.api.key}")
    private String apiKey;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    public String getHealthAdvice(String userMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "mistral-tiny");
        
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", "Berikan saran kesehatan untuk: " + userMessage + 
                         ". Jawab dalam bahasa Indonesia, maksimal 100 kata.");
        
        requestBody.put("messages", new Map[]{message});
        requestBody.put("max_tokens", 200);
        requestBody.put("temperature", 0.7);
        
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, request, Map.class);
            Map<String, Object> responseBody = response.getBody();
            
            if (responseBody != null && responseBody.containsKey("choices")) {
                var choices = (java.util.List<Map<String, Object>>) responseBody.get("choices");
                if (!choices.isEmpty()) {
                    var messageResponse = (Map<String, Object>) choices.get(0).get("message");
                    return (String) messageResponse.get("content");
                }
            }
            return "Maaf, tidak dapat memproses permintaan Anda saat ini.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}