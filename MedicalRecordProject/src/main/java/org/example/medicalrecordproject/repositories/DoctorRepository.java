package org.example.medicalrecordproject.repositories;

import org.example.medicalrecordproject.models.Specialty;
import org.example.medicalrecordproject.models.users.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    public Doctor findBySpecialtiesContains(Specialty specialty);

    public List<Doctor> findAllByIsGp(boolean isGp);
}
