package org.example.medicalrecordproject.models.users;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("ADMIN")
@Table(name = "admin")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Admin extends User {

    @Column(name = "contact_info")
    private String contactInfo;
}
