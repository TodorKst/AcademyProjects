package org.example.medicalrecordproject.models;

import lombok.*;
import jakarta.persistence.*;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "user_id")
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
