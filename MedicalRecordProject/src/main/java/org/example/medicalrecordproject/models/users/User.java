package org.example.medicalrecordproject.models.users;

import jakarta.persistence.*;
import lombok.*;
import org.example.medicalrecordproject.enums.UserRole;
import org.example.medicalrecordproject.models.BaseEntity;

import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users", schema = "medical_record")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class User extends BaseEntity {

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "name", length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", insertable = false, updatable = false) // Prevents conflicts with DiscriminatorColumn
    private UserRole role;
}
