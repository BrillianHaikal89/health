package com.example.health.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "patients")
public class Patient {
    @Id
    private String id;
    private String name;
    private int age;
    private String gender;
    private String medicalHistory;
    private String currentCondition;
    private LocalDate lastVisit;
    private String doctorNotes;

    public Patient() {}

    public Patient(String name, int age, String gender, String medicalHistory, 
                   String currentCondition, LocalDate lastVisit) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.medicalHistory = medicalHistory;
        this.currentCondition = currentCondition;
        this.lastVisit = lastVisit;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }
    
    public String getCurrentCondition() { return currentCondition; }
    public void setCurrentCondition(String currentCondition) { this.currentCondition = currentCondition; }
    
    public LocalDate getLastVisit() { return lastVisit; }
    public void setLastVisit(LocalDate lastVisit) { this.lastVisit = lastVisit; }
    
    public String getDoctorNotes() { return doctorNotes; }
    public void setDoctorNotes(String doctorNotes) { this.doctorNotes = doctorNotes; }
}