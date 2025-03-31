package org.example.medicalrecordproject.dtos.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientOutDto {
    private Long id;
    private String name;
}
