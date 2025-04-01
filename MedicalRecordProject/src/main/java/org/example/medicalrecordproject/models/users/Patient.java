package org.example.medicalrecordproject.models.users;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.medicalrecordproject.models.MedicalVisit;

import java.sql.Date;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "patient_profile", schema = "medical_record")
@DiscriminatorValue("PATIENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient extends User {

    @Column(name = "last_insurance_payment")
    private Date lastInsurancePayment;

    @NotNull(message = "General Practitioner (gp) is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gp_id")
    private Doctor gp;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<MedicalVisit> medicalVisits;

}
