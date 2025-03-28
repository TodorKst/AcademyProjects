package org.example.medicalrecordproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "diagnoses", schema = "medical_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diagnosis extends BaseEntity {

    @NotNull(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description")
    private String description;
}
