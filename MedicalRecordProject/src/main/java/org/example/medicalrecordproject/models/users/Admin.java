package org.example.medicalrecordproject.models.users;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
