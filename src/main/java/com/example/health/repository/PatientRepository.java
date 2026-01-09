package com.example.health.repository;

import com.example.health.model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PatientRepository extends MongoRepository<Patient, String> {
    List<Patient> findByNameContainingIgnoreCase(String name);
}