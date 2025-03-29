package org.example.medicalrecordproject.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "specialties", schema = "medical_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Specialty extends BaseEntity {

    @NotNull(message = "Specialty name cannot be null")
    @Size(max = 100, message = "Specialty name must be at most 100 characters")
    @Column(name = "name", nullable = false, length = 100, unique = true)
    private String name;
}
