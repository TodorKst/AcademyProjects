package org.example.medicalrecordproject.models.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("ADMIN")
@Table(name = "admin")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Admin extends User {

    @Column(name = "contact_info")
    private String contactInfo;
}
