package org.example.medicalrecordproject.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.medicalrecordproject.models.users.Doctor;
import org.example.medicalrecordproject.models.users.Patient;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true, exclude = {"doctor", "patient", "diagnoses"})
@ToString(exclude = {"doctor", "patient", "diagnoses"})
@Entity
@Table(name = "medical_visits", schema = "medical_record")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalVisit extends BaseEntity {

    @Column(name = "treatment")
    private String treatment;

    @NotNull(message = "Visit date is required")
    @Column(name = "visit_date", nullable = false)
    private LocalDateTime visitDate;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @NotNull(message = "Patient is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    @JsonBackReference
    private Patient patient;

    @NotNull(message = "Doctor is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    @JsonBackReference
    private Doctor doctor;

    @ManyToMany
    @JoinTable(
            name = "medical_visit_diagnoses",
            schema = "medical_record",
            joinColumns = @JoinColumn(name = "medical_visit_id"),
            inverseJoinColumns = @JoinColumn(name = "diagnosis_id")
    )
    @Builder.Default
    @JsonManagedReference
    private Set<Diagnosis> diagnoses = new HashSet<>();
}
