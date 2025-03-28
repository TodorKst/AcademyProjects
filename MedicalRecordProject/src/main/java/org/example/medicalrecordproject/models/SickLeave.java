package org.example.medicalrecordproject.models;

import jakarta.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import lombok.*;

@Entity
@Table(name = "sick_leave_records", schema = "medical_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SickLeave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_visit_id")
    private MedicalVisit medicalVisit;
}
