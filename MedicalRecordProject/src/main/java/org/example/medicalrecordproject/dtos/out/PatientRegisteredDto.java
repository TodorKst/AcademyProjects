package org.example.medicalrecordproject.dtos.out;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientRegisteredDto {
    private Long id;
    private String name;
    private String username;
    private Date lastInsurancePayment;
    private Long gpId;
    private String gpName;
}
