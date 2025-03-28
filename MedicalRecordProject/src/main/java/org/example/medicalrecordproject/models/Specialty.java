package org.example.medicalrecordproject.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "specialties", schema = "medical_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Specialty extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100, unique = true)
    private String name;

}
