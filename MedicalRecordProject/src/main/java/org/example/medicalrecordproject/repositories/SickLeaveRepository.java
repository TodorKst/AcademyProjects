package org.example.medicalrecordproject.repositories;

import org.example.medicalrecordproject.models.SickLeave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SickLeaveRepository extends JpaRepository<SickLeave, Long> {

    @Query("""
            SELECT FUNCTION('MONTH', s.startDate), COUNT(s)
            FROM SickLeave s
            GROUP BY FUNCTION('MONTH', s.startDate)
            ORDER BY COUNT(s) DESC
            """)
    List<Object[]> getMonthWithMostSickLeaves();

    @Query("""
    SELECT mv.doctor.id, COUNT(sl)
    FROM SickLeave sl
    JOIN sl.medicalVisit mv
    GROUP BY mv.doctor.id
    ORDER BY COUNT(sl) DESC
""")
    List<Object[]> getTopDoctorsBySickLeaves();

}
