package org.example.medicalrecordproject.repositories;

import org.example.medicalrecordproject.models.users.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
