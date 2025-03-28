package org.example.medicalrecordproject.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "specialties", schema = "medical_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Specialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false, length = 100, unique = true)
    private String name;
}
