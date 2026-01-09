package com.example.health.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.health.model.Patient;

public interface PatientRepository extends MongoRepository<Patient, String> {

    List<Patient> findByNameContainingIgnoreCase(String name);

    List<Patient> findByLastVisit(String date); 
}
