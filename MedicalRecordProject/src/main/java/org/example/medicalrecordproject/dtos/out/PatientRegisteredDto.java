package org.example.medicalrecordproject.dtos.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
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
