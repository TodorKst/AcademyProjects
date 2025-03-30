package org.example.medicalrecordproject.models.users;

import lombok.*;
import jakarta.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("ADMIN")
@Data
@NoArgsConstructor
public class Admin extends User {
    // No additional fields for Admin
}
