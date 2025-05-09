package org.example.medicalrecordproject.dtos.out.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponseDto {
    private Long id;
    private String name;
    private String username;
    private Date lastInsurancePayment;
    private Long gpId;
    private String gpName;
}
