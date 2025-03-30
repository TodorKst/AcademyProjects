package org.example.medicalrecordproject.models.users;

import jakarta.persistence.*;
import lombok.*;
import org.example.medicalrecordproject.models.MedicalVisit;
import org.example.medicalrecordproject.models.Specialty;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "doctor_profile", schema = "medical_record")
@DiscriminatorValue("DOCTOR")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor extends User {

    @Column(name = "is_gp")
    private Boolean isGp;

    @ManyToMany
    @JoinTable(
            name = "doctor_specialties",
            schema = "medical_record",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "specialty_id")
    )
    @Builder.Default
    private Set<Specialty> specialties = new HashSet<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MedicalVisit> medicalVisits;

    @OneToMany(mappedBy = "gp", fetch = FetchType.LAZY)
    private List<Patient> patients;

}
