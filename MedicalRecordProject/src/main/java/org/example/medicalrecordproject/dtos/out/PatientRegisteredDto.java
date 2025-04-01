package org.example.medicalrecordproject.dtos.out;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
