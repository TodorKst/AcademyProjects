package org.example.medicalrecordproject.dtos.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisStatOutDto {
    private String name;
    private Long count;
}
