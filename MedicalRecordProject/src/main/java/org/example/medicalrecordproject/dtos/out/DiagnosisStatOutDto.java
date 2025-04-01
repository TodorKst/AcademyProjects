package org.example.medicalrecordproject.dtos.out;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisStatOutDto {
    private String name;
    private Long count;
}
