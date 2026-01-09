package com.example.health;

import com.example.health.model.User;
import com.example.health.model.Patient;
import com.example.health.repository.UserRepository;
import com.example.health.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class HealthApplication implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    public static void main(String[] args) {
        SpringApplication.run(HealthApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // Initialize default admin user
        if (userRepository.count() == 0) {
            User admin = new User("brillian", "admin123", "ADMIN");
            userRepository.save(admin);
            System.out.println("Default admin user created");
        }

        // Initialize some sample patients
        if (patientRepository.count() == 0) {
            patientRepository.save(new Patient("Budi Santoso", 35, "Laki-laki",
                    "Hipertensi, Diabetes", "Stabil", LocalDate.now()));

            patientRepository.save(new Patient("Siti Aminah", 45, "Perempuan",
                    "Asma, Alergi", "Butuh kontrol rutin", LocalDate.now()));

            patientRepository.save(new Patient("Ahmad Rizki", 28, "Laki-laki",
                    "Tidak ada", "Flu ringan", LocalDate.now()));

            System.out.println("Sample patients created");
        }
    }
}
