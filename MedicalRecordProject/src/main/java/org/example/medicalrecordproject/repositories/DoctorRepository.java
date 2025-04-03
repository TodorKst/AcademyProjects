package org.example.medicalrecordproject.repositories;

import org.example.medicalrecordproject.models.Specialty;
import org.example.medicalrecordproject.models.users.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findAllBySpecialtiesContains(Specialty specialty);

    List<Doctor> findAllByIsGp(boolean isGp);

    Optional<Doctor> findByUsername(String username);

    @Query("""
    SELECT mv.doctor
    FROM SickLeave sl
    JOIN sl.medicalVisit mv
    GROUP BY mv.doctor
    HAVING COUNT(sl) = (
        SELECT MAX(sickLeaveCount) FROM (
            SELECT COUNT(sl2) AS sickLeaveCount
            FROM SickLeave sl2
            JOIN sl2.medicalVisit mv2
            GROUP BY mv2.doctor
        )
    )
    """)
    List<Doctor> findDoctorsWithMostSickLeaves();

    boolean existsByUsername(String username);
}
