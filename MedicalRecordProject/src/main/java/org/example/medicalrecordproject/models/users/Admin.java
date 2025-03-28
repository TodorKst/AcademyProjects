package org.example.medicalrecordproject.models.users;

import lombok.*;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ADMIN")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin extends User {
    // No additional fields for Admin
}
