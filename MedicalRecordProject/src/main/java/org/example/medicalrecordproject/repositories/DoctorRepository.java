package org.example.medicalrecordproject.repositories;

import org.example.medicalrecordproject.models.Specialty;
import org.example.medicalrecordproject.models.users.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    public List<Doctor> findAllBySpecialtiesContains(Specialty specialty);

    public List<Doctor> findAllByIsGp(boolean isGp);

    Optional<Doctor> findByUsername(String username);

    @Query("""
            SELECT mv.doctor
            FROM SickLeave sl
            JOIN sl.medicalVisit mv
            GROUP BY mv.doctor
            ORDER BY COUNT(sl) DESC
            """)
    List<Doctor> findTopDoctorsBySickLeaves(Pageable pageable);

//    Use like this to return top 5 doctors that have writen most sick leaves
//    Pageable pageable = PageRequest.of(0, 5);
//    List<Doctor> doctors = doctorRepository.findTopDoctorsBySickLeaves(pageable);

}
